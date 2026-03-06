package org.jts.filter;

import org.jts.pkg.PkgErr;
import org.jts.pkg.PkgErrRes;
import org.jts.pkg.PkgResHead;
import org.jts.util.JsonUtils;
import org.jts.util.PbElExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.jts.pkg.PkgErr.TECH_ERROR_TYPE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_DEF_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_RETURNCODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_JSON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_GID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_LID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_PID;

/**
 * 根据请求体内容，使用表达式进行过滤，命中表达式进行阻拦，返回报错信息
 * 配置示例
 * filters:
 *   - name: ReqBlock
 *     args:
 *       enable: true
 *       expStr: "#ctx[wadawd] == '213213'"
 */
public class ReqBlockGatewayFilterFactory extends AbstractGatewayFilterFactory<ReqBlockGatewayFilterFactory.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReqBlockGatewayFilterFactory.class);

    private static final String CTX_FLAG = "ctx";

    private static final String URL_KEY = "_url";

    List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    public ReqBlockGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            if (!config.isEnable()) {
                return chain.filter(exchange);
            }

            return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, serverHttpRequest -> {

                // 构建缓存后的 ServerWebExchange
                ServerWebExchange cachedExchange = exchange.mutate().request(serverHttpRequest).build();

                return ServerRequest.create(cachedExchange, messageReaders)
                        .bodyToMono(String.class)
                        .flatMap(objectValue -> {
                            // 使用缓存后的 exchange
                            ServerHttpResponse res = cachedExchange.getResponse();
                                String expStr = config.getExpStr();
                                try {
                                    ServerHttpRequest req = cachedExchange.getRequest();
                                    Map<String, Object> reqMap = JsonUtils.strToMap(objectValue);
                                    reqMap.put(URL_KEY, req.getPath().value());

                                    StandardEvaluationContext context = new StandardEvaluationContext();
                                    context.setVariable(CTX_FLAG, reqMap);

                                    boolean resFlag = PbElExpressionParser.getBoolVal(expStr, context);
                                    LOGGER.info("expStr[{}] evaluate result:{}", expStr, resFlag);
                                    if (resFlag) {
                                        return handleResponse(req.getHeaders(), reqMap, res);
                                    }
                                } catch (Exception e) {
                                    LOGGER.error("expStr[{}] parseExpression error", expStr, e);
                                }
                            // 传递缓存后的 exchange 到下游
                            return chain.filter(cachedExchange);
                        });
            });
        };
    }

    private Mono<Void> handleResponse(HttpHeaders reqHttpHeaders,Map<String,Object> reqMap,ServerHttpResponse res) {
        String retCode = GW_DEF_ERR_CODE;
        res.setStatusCode(HttpStatus.OK);
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
        pkgErr.setDescription("当前请求被网关技术性拦截");

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

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enable","expStr");
    }

    public static class Config {
        boolean enable;
        String expStr;

        public Config(boolean enable,String expStr) {
            this.enable = enable;
            this.expStr = expStr;
        }

        public boolean getFlag() {
            return enable;
        }

        public void setFlag(boolean enable) {
            this.enable = enable;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getExpStr() {
            return expStr;
        }

        public void setExpStr(String expStr) {
            this.expStr = expStr;
        }
    }
}
