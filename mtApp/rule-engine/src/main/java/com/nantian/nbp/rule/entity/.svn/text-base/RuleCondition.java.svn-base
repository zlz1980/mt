package com.nantian.nbp.rule.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

public class RuleCondition implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleCondition.class);
    private String bizType;
    private String condName;
    private String condExp;

    private double complexity;

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

    public String getCondExp() {
        return condExp;
    }

    public void setCondExp(String condExp) {
        this.condExp = condExp;
    }

    public void initComplexity() {
        if (Objects.nonNull(condExp)) {
            int countAnd = 0;
            int countOr = 0;
            for (int i = 0; i < condExp.length(); i++) {
                if (i + 1 >= condExp.length()) {
                    break;
                }
                char c1 = condExp.charAt(i);
                char c2 = condExp.charAt(i + 1);
                if (c1 == '&' && c2 == '&') {
                    countAnd++;
                } else if (c1 == '|' && c2 == '|') {
                    countOr++;
                }
            }
            this.complexity = countAnd + countOr + 1;
        }
    }

    public RuleConditionKey getRuleConditionKey() {
        return RuleConditionKey.of(bizType, condName);
    }

    public double getComplexity() {
        return complexity;
    }
}
