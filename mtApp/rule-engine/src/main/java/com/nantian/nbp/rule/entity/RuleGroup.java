package com.nantian.nbp.rule.entity;

import java.util.LinkedList;
import java.util.List;

public class RuleGroup {
    private RuleGroupKey ruleGroupKey;

    private List<Rule> ruleList = new LinkedList<>();

    public RuleGroup(RuleGroupKey ruleGroupKey) {
        this.ruleGroupKey = ruleGroupKey;
    }

    public RuleGroupKey getRuleGroupKey() {
        return ruleGroupKey;
    }

    public void setRuleGroupKey(RuleGroupKey ruleGroupKey) {
        this.ruleGroupKey = ruleGroupKey;
    }

    public void add(Rule rule) {
        ruleList.add(rule);
    }

    public void sortRuleListByRuleComplexity() {
        ruleList.sort((o1, o2) -> Double.compare(o2.getComplexity(), o1.getComplexity()));
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }
}
