package com.nantian.nbp.rule.entity;

import com.nantian.nbp.rule.ast.node.ExpressionNode;
import com.nantian.nbp.rule.ast.ExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.nantian.nbp.rule.RuleBizConstants.BRACES_PATTERN;

public class Rule implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger("ruleCache");
    private String bizType;
    private String groupId;
    private String ruleId;

    /* Rule中的表达式定义只接受{}构成的逻辑表达式，不接受常量形式 */
    private String expStr;
    private String result;
    private double complexity;

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    /* complexity计数规则：以{}的个数为准，如果{}中对应的条件中包含不同运算符组成的组合表达式，则每个运算符均参与累计计数 */
    public void initComplexity(RuleBizType ruleBizType) {
        StringBuilder expStrBuilder = new StringBuilder(expStr);
        Matcher m = BRACES_PATTERN.matcher(expStrBuilder);
        int ruleExpCount = 0;
        int ruleExpAllCondCount = 0;
        while (m.find()) {
            String tmp = m.group(0);
            String condName = tmp.substring(1, tmp.length() - 1);
            RuleCondition ruleCondition = ruleBizType.getRuleCondition(bizType, condName.trim());
            if (Objects.nonNull(ruleCondition)) {
                ruleExpAllCondCount += ruleCondition.getComplexity();
            } else {
                throw new RuntimeException(String.format("rule[%s_%s_%s] not found condName[%s]  ", bizType, groupId, ruleId, condName));
            }
            ruleExpCount++;
        }
        this.complexity = ruleExpAllCondCount + ruleExpCount;
    }

    /**
     * 计算表达式复杂度（返回 -1 表示非法）
     */
    public void computeComplexity(RuleBizType ruleBizType) {
        Map<String, Double> ruleConditionWeights = getRuleConditionWeights(ruleBizType);
        ExpressionParser parser = new ExpressionParser(ruleConditionWeights);
        ExpressionNode ast = parser.parse(expStr);
        complexity = ast.computeComplexity();
        LOGGER.info("complexity:[{}]  rule:[{}_{}_{}]  expStr:[{}]", complexity, bizType, groupId, ruleId, expStr);
        LOGGER.info(ast.print("", true));
    }


    public Map<String, Double> getRuleConditionWeights(RuleBizType ruleBizType) {
        Map<String, Double> atomWeights = new HashMap<>();
        StringBuilder expStrBuilder = new StringBuilder(expStr);
        Matcher m = BRACES_PATTERN.matcher(expStrBuilder);
        while (m.find()) {
            String tmp = m.group(0);
            String condName = tmp.substring(1, tmp.length() - 1);
            RuleCondition ruleCondition = ruleBizType.getRuleCondition(bizType, condName.trim());
            if (Objects.nonNull(ruleCondition)) {
                atomWeights.put(condName, ruleCondition.getComplexity());
            } else {
                throw new RuntimeException(String.format("rule:[%s_%s_%s] not found condName:[%s]  ", bizType, groupId, ruleId, condName));
            }
        }
        return atomWeights;
    }


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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getExpStr() {
        return expStr;
    }

    public void setExpStr(String expStr) {
        this.expStr = expStr;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
