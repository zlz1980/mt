package org.jts.sentinel.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.jts.pkg.PkgErr;
import org.jts.pkg.PkgErrRes;
import org.jts.pkg.PkgResHead;
import org.jts.util.JsonUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jts.pkg.PkgErr.TECH_ERROR_TYPE;
import static org.jts.pkg.PkgHttpHeadKeys.GW_LIMIT_ERR_CODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_RETURNCODE;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_COMMON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_JSON_VERSION;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_GID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_LID;
import static org.jts.pkg.PkgHttpHeadKeys.PKG_TRACE_PID;

@Configuration
@EnableConfigurationProperties(SentinelGatewayProperties.class)
public class SentinelLocalConfig {

    private final SentinelGatewayProperties sentinelGatewayProperties;

    public SentinelLocalConfig(SentinelGatewayProperties sentinelGatewayProperties) {
        this.sentinelGatewayProperties = sentinelGatewayProperties;
    }

    /**
     * 初始化网关限流规则（路由维度）
     */
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        for (GatewayFlowRuleConfig config : sentinelGatewayProperties.getRules()) {
            GatewayFlowRule rule = new GatewayFlowRule(config.getResourceId())
                    .setCount(config.getCount())
                    .setIntervalSec(config.getIntervalSec())
                    .setGrade(RuleConstant.FLOW_GRADE_QPS)
                    .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
            rules.add(rule);
        }

        GatewayRuleManager.loadRules(rules);

        /*Set<GatewayFlowRule> rules = new HashSet<>();
        // 规则1：对用户服务路由限流（QPS=5）
        // resource对应路由ID
        GatewayFlowRule userRule = new GatewayFlowRule("omServiceTranV1")
                // QPS阈值
                .setCount(1)
                // 统计窗口1秒
                .setIntervalSec(1)
                // 按QPS限流
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                 // 快速失败
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(userRule);
        // 规则2：对订单服务路由限流（线程数=10）
        GatewayFlowRule orderRule = new GatewayFlowRule("order_route")
                .setCount(10)
                // 按线程数限流
                .setGrade(RuleConstant.FLOW_GRADE_THREAD)
                .setIntervalSec(1);
        rules.add(orderRule);
        // 加载规则到内存
        GatewayRuleManager.loadRules(rules);*/
    }

    /**
     * 初始化自定义API分组规则
     */
    @PostConstruct
    public void initCustomApiRules() {
        // 1. 定义API分组（合并多个路径）
        /*ApiDefinition apiGroup = new ApiDefinition("custom_api_group")
                .setPredicateItems(Collections.singleton(
                        new ApiPathPredicateItem()
                                .setPattern("/product/**")       // 匹配/product/下所有路径
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)
                ));

        GatewayApiDefinitionManager.loadApiDefinitions(
                Collections.singleton(apiGroup)
        );*/

        // 2. 为该分组配置限流规则（QPS=20）
        /*GatewayFlowRule apiRule = new GatewayFlowRule("custom_api_group")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(20)
                .setGrade(RuleConstant.FLOW_GRADE_QPS);

        GatewayRuleManager.loadRules(Collections.singleton(apiRule));*/
    }

    /**
     * 注册Sentinel过滤器
     */
    /*@Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }*/

    /**
     * 注册限流异常处理器
     */
   /* @Bean
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
                return handleResponse(reqHttpHeaders, response);
            }
        };
    }*/

    private Mono<Void> handleResponse(HttpHeaders reqHttpHeaders,ServerHttpResponse res) {
        String retCode = GW_LIMIT_ERR_CODE;
        res.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
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
        pkgErr.setDescription("当前请求被网关技术性限流");

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
