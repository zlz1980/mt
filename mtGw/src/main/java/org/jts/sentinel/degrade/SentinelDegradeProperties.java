package org.jts.sentinel.degrade;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sentinel.gateway.degrade")
public class SentinelDegradeProperties {
    private List<GatewayDegradeRuleConfig> rules = new ArrayList<>();

    public List<GatewayDegradeRuleConfig> getRules() {
        return rules;
    }

    public void setRules(List<GatewayDegradeRuleConfig> rules) {
        this.rules = rules;
    }
}
