package org.jts.sentinel.flow;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableConfigurationProperties(SentinelFlowProperties.class)
public class FlowLocalConfig {

    private final SentinelFlowProperties sentinelFlowProperties;

    public FlowLocalConfig(SentinelFlowProperties sentinelFlowProperties) {
        this.sentinelFlowProperties = sentinelFlowProperties;
    }

    /**
     * 初始化网关限流规则（路由维度）
     */
    @PostConstruct
    public void initGatewayRules() {
        List<GatewayFlowRuleConfig> rules = sentinelFlowProperties.getRules();
        Set<GatewayFlowRule> flowRules = new HashSet<>(rules.size());
        for (GatewayFlowRuleConfig config : rules) {
            GatewayFlowRule rule = new GatewayFlowRule(config.getResourceId())
                    .setGrade(config.getGrade())
                    .setCount(config.getCount())
                    .setControlBehavior(config.getControlBehavior())
                    .setIntervalSec(config.getIntervalSec());
            flowRules.add(rule);
        }
        GatewayRuleManager.loadRules(flowRules);

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
}
