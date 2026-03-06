package org.jts.sentinel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sentinel.gateway")
public class SentinelGatewayProperties {
    private List<GatewayFlowRuleConfig> rules = new ArrayList<>();

    public List<GatewayFlowRuleConfig> getRules() {
        return rules;
    }

    public void setRules(List<GatewayFlowRuleConfig> rules) {
        this.rules = rules;
    }
}
