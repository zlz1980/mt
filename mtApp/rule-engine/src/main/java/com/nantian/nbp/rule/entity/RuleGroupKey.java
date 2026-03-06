package com.nantian.nbp.rule.entity;

import java.util.Objects;

public class RuleGroupKey {
    private String groupId;
    private String bizType;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleGroupKey that = (RuleGroupKey) o;
        return Objects.equals(bizType, that.bizType) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, bizType);
    }

    @Override
    public String toString() {
        return bizType+"_"+groupId;
    }
    public static RuleGroupKey of(String bizType, String groupId){
        RuleGroupKey ruleGroupKey = new RuleGroupKey();
        ruleGroupKey.setBizType(bizType);
        ruleGroupKey.setGroupId(groupId);
        return ruleGroupKey;
    }
}
