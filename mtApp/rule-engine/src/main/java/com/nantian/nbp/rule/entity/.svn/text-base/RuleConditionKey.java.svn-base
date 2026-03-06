package com.nantian.nbp.rule.entity;

import java.util.Objects;

public class RuleConditionKey {
    private String bizType;
    private String condName;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCondName() {
        return condName;
    }

    public void setCondName(String condName) {
        this.condName = condName;
    }

    @Override
    public String toString() {
        return bizType+"_"+ condName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleConditionKey ruleConditionKey = (RuleConditionKey) o;
        return Objects.equals(bizType, ruleConditionKey.bizType) && Objects.equals(condName, ruleConditionKey.condName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bizType, condName);
    }

    public static RuleConditionKey of(String bizType, String ruleName){
        RuleConditionKey ruleSetKey = new RuleConditionKey();
        ruleSetKey.setBizType(bizType);
        ruleSetKey.setCondName(ruleName);
        return ruleSetKey;
    }
}
