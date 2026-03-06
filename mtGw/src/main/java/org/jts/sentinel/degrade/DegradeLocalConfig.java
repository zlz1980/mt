package org.jts.sentinel.degrade;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SentinelDegradeProperties.class)
public class DegradeLocalConfig {

    private final SentinelDegradeProperties sentinelDegradeProperties;

    public DegradeLocalConfig(SentinelDegradeProperties sentinelDegradeProperties) {
        this.sentinelDegradeProperties = sentinelDegradeProperties;
    }

    @PostConstruct
    public void init() {
        List<GatewayDegradeRuleConfig> rules = sentinelDegradeProperties.getRules();
        List<DegradeRule> degradeRules = new ArrayList<>(rules.size());
        for (GatewayDegradeRuleConfig config : rules){
            DegradeRule degradeRule = new DegradeRule(config.getResourceId())
                    .setGrade(config.getGrade())
                    // 异常比例超过10%熔断
                    .setCount(config.getCount())
                    .setStatIntervalMs(config.getStatIntervalMs())
                    .setSlowRatioThreshold(config.getSlowRatioThreshold())
                    .setTimeWindow(config.getTimeWindow())
                    .setMinRequestAmount(config.getMinRequestAmount());
            degradeRules.add(degradeRule);
        }
        DegradeRuleManager.loadRules(degradeRules);
        /*// 配置基于响应时间的熔断
        DegradeRule rtRule = new DegradeRule("user-service")
                .setGrade(RuleConstant.DEGRADE_GRADE_RT)
                // 响应时间超过5秒熔断
                .setCount(5000)
                // 熔断10秒
                .setTimeWindow(10)
                // 最少10个请求
                .setMinRequestAmount(10);

        // 配置基于异常比例的熔断
        DegradeRule exceptionRule = new DegradeRule("order-service")
                .setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO)
                // 异常比例超过10%熔断
                .setCount(0.1)
                .setTimeWindow(10)
                .setMinRequestAmount(10);
        DegradeRuleManager.loadRules(Arrays.asList(rtRule, exceptionRule));*/
    }
}
