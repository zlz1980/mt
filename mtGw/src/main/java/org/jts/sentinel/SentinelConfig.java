package org.jts.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import org.jts.pkg.PkgErr;
import org.jts.pkg.PkgErrRes;
import org.jts.pkg.PkgResHead;
import org.jts.sentinel.degrade.DegradeLocalConfig;
import org.jts.sentinel.flow.FlowLocalConfig;
import org.jts.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.jts.pkg.PkgErr.TECH_ERROR_TYPE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_DEF_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_DEGRADE_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_LIMIT_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_RETURNCODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_JSON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_GID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_LID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_PID;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Configuration
@Import({FlowLocalConfig.class, DegradeLocalConfig.class})
public class SentinelConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentinelConfig.class);
    /**
     * 注册Sentinel过滤器
     */
    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }
    /*@Bean
    @Order(-1)
    public SentinelGatewayFilterFactory sentinelGatewayFilterFactory() {
        return new SentinelGatewayFilterFactory();
    }*/

    /**
     * 注册限流异常处理器
     * 作用: 创建 Sentinel 网关过滤器工厂
     * 功能: 将 Sentinel 的限流规则应用到 Gateway 路由上
     * 执行顺序: @Order(-1) 确保优先执行
     */
    @Bean
    @Order(-1)
    public SentinelGatewayBlockExceptionHandler blockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(
                Collections.emptyList(),
                new DefaultServerCodecConfigurer()
        ){
            @Override
            public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
                HttpHeaders reqHttpHeaders = exchange.getRequest().getHeaders();
                ServerHttpResponse response = exchange.getResponse();
                /**
                 * 限流异常处理
                 * 触发条件: 请求超过流控阈值
                 * HTTP 状态码: 429 TOO_MANY_REQUESTS
                 * 错误代码: GW_LIMIT_ERR_CODE
                 * 响应消息: "限流"
                 */
                if(ex instanceof ParamFlowException){
                    //ParamFlowException参数限流异常处理
                    ParamFlowException e = (ParamFlowException) ex;
                    LOGGER.error("[{}]Flow limit exception.",e.getResourceName());
                    return handleResponse(TOO_MANY_REQUESTS,GW_LIMIT_ERR_CODE,reqHttpHeaders, response,"限流");
                    /**
                     * 熔断异常处理
                     * 触发条件: 服务降级（异常率超过阈值）
                     * HTTP 状态码: 500 INTERNAL_SERVER_ERROR
                     * 错误代码: GW_DEGRADE_ERR_CODE
                     * 响应消息: "熔断"
                     */
                }else if(ex instanceof DegradeException){
                    //DegradeException熔断异常处理
                    DegradeException e = (DegradeException) ex;
                    LOGGER.error("Degrade exception.",e);
                    return handleResponse(INTERNAL_SERVER_ERROR,GW_DEGRADE_ERR_CODE,reqHttpHeaders, response,"熔断");
                }
                LOGGER.error("blockException.",ex);
                return handleResponse(INTERNAL_SERVER_ERROR,GW_DEF_ERR_CODE,reqHttpHeaders, response,"其他错误");
            }
        };
    }

    private Mono<Void> handleResponse(HttpStatus status, String retCode, HttpHeaders reqHttpHeaders, ServerHttpResponse res, String msg) {
        res.setStatusCode(status);
        HttpHeaders httpHeaders = res.getHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> gidList = reqHttpHeaders.get(PKG_TRACE_GID);
        if(!CollectionUtils.isEmpty(gidList)){
            httpHeaders.set(PKG_TRACE_GID,gidList.get(0));
        }
        List<String> reqLidList = reqHttpHeaders.get(PKG_TRACE_LID);
        if(!CollectionUtils.isEmpty(reqLidList)){
            httpHeaders.set(PKG_TRACE_PID,reqLidList.get(0));
        }
        httpHeaders.set(PKG_TRACE_LID,"");
        httpHeaders.set(PKG_COMMON_RETURNCODE,retCode);
        httpHeaders.set(PKG_COMMON_VERSION, PKG_JSON_VERSION);

        PkgErr pkgErr = new PkgErr();
        pkgErr.setCode(retCode);
        pkgErr.setType(TECH_ERROR_TYPE);
        pkgErr.setMessage("请求处理需联系应用管理员，请稍后再试！");
        pkgErr.setDescription(String.format("当前请求被网关技术性%s",msg));

        PkgResHead head = new PkgResHead();
        head.setReturnCode(retCode);

        PkgErrRes errRes =new PkgErrRes();
        errRes.setHead(head);
        errRes.setError(pkgErr);
        String responseBody = JsonUtils.objToString(errRes);
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = res.bufferFactory().wrap(bytes);
        return res.writeWith(Mono.just(buffer));
    }
}
