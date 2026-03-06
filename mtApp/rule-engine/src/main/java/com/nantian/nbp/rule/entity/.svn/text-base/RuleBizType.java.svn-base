package com.nantian.nbp.rule.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleBizType implements Serializable {
    private String bizType;
    private String groupIdExp;
    private final Map<RuleConditionKey, RuleCondition> ruleCondMap = new HashMap<>();

    private final Map<RuleGroupKey,RuleGroup> ruleGroupMap = new HashMap<>();

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getGroupIdExp() {
        return groupIdExp;
    }

    public void setGroupIdExp(String groupIdExp) {
        this.groupIdExp = groupIdExp;
    }

    public void addRuleCondition(RuleCondition ruleCondition) {
        ruleCondMap.put(ruleCondition.getRuleConditionKey(), ruleCondition);
    }

    public RuleCondition getRuleCondition(String bizType, String condName) {
        return ruleCondMap.get(RuleConditionKey.of(bizType,condName));
    }

    public void addRuleGroup(RuleGroup ruleGroup) {
        ruleGroupMap.put(ruleGroup.getRuleGroupKey(), ruleGroup);
    }
    public RuleGroup getRuleGroup(String bizType, String groupId) {
        return ruleGroupMap.get(RuleGroupKey.of(bizType,groupId));
    }

    public Map<RuleGroupKey, RuleGroup> getRuleGroupMap() {
        return ruleGroupMap;
    }
}
