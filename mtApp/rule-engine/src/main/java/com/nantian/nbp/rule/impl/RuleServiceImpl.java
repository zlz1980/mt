package com.nantian.nbp.rule.impl;

import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbElExpressionParser;
import com.nantian.nbp.ev.PbEvContext;
import com.nantian.nbp.rule.RuleDataService;
import com.nantian.nbp.rule.RuleService;
import com.nantian.nbp.rule.entity.Rule;
import com.nantian.nbp.rule.entity.RuleBizType;
import com.nantian.nbp.rule.entity.RuleCondition;
import com.nantian.nbp.rule.entity.RuleGroup;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.nantian.nbp.rule.RuleBizConstants.APP_RULE_ENGINE_ERR_KEY;
import static com.nantian.nbp.rule.RuleBizConstants.BRACES_PATTERN;
import static com.nantian.nbp.utils.Constants.CTX_FLAG;
import static com.nantian.nbp.utils.Constants.EL_CTX_FLAG;
import static com.nantian.nbp.utils.Constants.PARAM_SPILT_FLAG;

public class RuleServiceImpl implements RuleService {
    private static final String ELVAR_START_FLAG = "${";
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleServiceImpl.class);


    private final RuleDataService ruleDataService;

    public RuleServiceImpl(RuleDataService ruleDataService) {
        this.ruleDataService = ruleDataService;
    }

    private PbEvContext createEvContext(Map<String, Object> data) {
        PbEvContext ctx = new PbEvContext();
        ctx.setVariable(CTX_FLAG, data);
        return ctx;
    }

    @Override
    public Rule findRule(String bizType, Map<String, Object> data) {
        return findRule(bizType, createEvContext(data));
    }

    @Override
    public String findRuleResult(String bizType, Map<String, Object> data) {
        return findRuleResult(bizType, createEvContext(data));
    }

    @Override
    public Rule findRule(String bizType, PbEvContext evContext) {
        RuleBizType ruleBizType = ruleDataService.getRuleBizType(bizType);
        if (Objects.isNull(ruleBizType)) {
            LOGGER.error(APP_RULE_ENGINE_ERR_KEY + "rule bizType[{}] is not found.", bizType);
            return null;
        }
        LOGGER.debug("find rule bizType[{}]", bizType);
        return calculateResult(ruleBizType, evContext);
    }

    @Override
    public String findRuleResult(String bizType, PbEvContext evContext) {
        Rule rule = findRule(bizType, evContext);
        if (rule == null) {
            return null;
        }
        String result = rule.getResult();
        if (!result.contains(ELVAR_START_FLAG)) {
            return result;
        }
        try {
            String repExpStr = MapUtils.repElVal(result);
            return (String) PbElExpressionParser.getVal(repExpStr, evContext).getVal();
        } catch (Exception e) {
            // 解析错误，抛出异常，由调用者捕获
            LOGGER.error(APP_RULE_ENGINE_ERR_KEY + "解析规则表达式失败，bizType[{}], resultExpStr[{}]", bizType, result,e);
        }
        return null;
    }

    private Rule calculateResult(RuleBizType ruleBizType, PbEvContext ctx) {
        String[] groupIds = StringUtils.delimitedListToStringArray(ruleBizType.getGroupIdExp(), PARAM_SPILT_FLAG);
        int idx = groupIds.length - 1;
        String[] groupIdsVal = initGroupIdsVal(groupIds, ctx);
        while (idx >= 0) {
            String currentGroupId = goBackGroupId(idx, groupIdsVal);
            LOGGER.info("query groupId[{}]", currentGroupId);
            Rule rule = findFirstResultByGroupId(ruleBizType, currentGroupId, ctx);
            if (Objects.nonNull(rule)) {
                LOGGER.info("find groupId[{}], ruleId[{}] res[{}]", currentGroupId, rule.getRuleId(), rule.getResult());
                return rule;
            }
            idx--;
        }
        LOGGER.error(APP_RULE_ENGINE_ERR_KEY + "Can not find matching result in [{}]", ruleBizType.getBizType());
        return null;
    }

    private String[] initGroupIdsVal(String[] groupIds, PbEvContext ctx) {
        int length = groupIds.length;
        String[] vals = new String[length];
        for (int i = 0; i < length; i++) {
            String exp = MapUtils.repElVal(groupIds[i]);
            String val;
            if (!exp.contains(EL_CTX_FLAG)) {
                val = exp;
            } else {
                val = StrUtils.toStrDefBlank(PbElExpressionParser.getVal(exp, ctx).getVal());
            }
            vals[i] = val;
        }
        return vals;
    }

    private String goBackGroupId(int idx, String[] groupIds) {
        StringBuilder groupId = new StringBuilder();
        for (int i = 0; i <= idx; i++) {
            groupId.append(groupIds[i]);
        }
        return groupId.toString();
    }

    private Boolean getRuleCondValByCache(RuleBizType ruleBizType, String condName, Map<String, Boolean> resTmp,
            PbEvContext data) {
        String bizType = ruleBizType.getBizType();
        Boolean ruleRes = resTmp.get(condName);
        if (Objects.isNull(ruleRes)) {
            RuleCondition ruleCondition = ruleBizType.getRuleCondition(bizType, condName);
            if (Objects.isNull(ruleCondition)) {
                throw new RuntimeException(String.format("bizType[%s],condName[%s] not found", bizType, condName));
            }
            String exp = MapUtils.repElVal(ruleCondition.getCondExp());
            ruleRes = PbElExpressionParser.getBoolVal(exp, data);
            resTmp.put(condName, ruleRes);
        }
        return ruleRes;
    }

    private String getRuleExp(RuleBizType ruleBizType, Rule rule, PbEvContext ctx, Map<String, Boolean> resTmp) {
        String expStr = rule.getExpStr();
        StringBuilder expStrBuilder = new StringBuilder(expStr);
        Matcher m = BRACES_PATTERN.matcher(expStrBuilder);
        while (m.find()) {
            String tmp = m.group(0);
            if (tmp == null) {
                throw new RuntimeException(String.format("valEl[%s] parsing err", expStr));
            }
            String condName = tmp.substring(1, tmp.length() - 1);
            Boolean ruleRes = getRuleCondValByCache(ruleBizType, condName, resTmp, ctx);
            if (ruleRes == null) {
                LOGGER.error(APP_RULE_ENGINE_ERR_KEY + "getRuleCondValByCache: bizType[{}],condName[{}] exp can not be parsed by PbElExpressionParser",
                        ruleBizType.getBizType(), condName);
                return null;
            }
            expStrBuilder.replace(m.start(), m.end(), ruleRes.toString());
            m = BRACES_PATTERN.matcher(expStrBuilder);
        }
        return expStrBuilder.toString();
    }

    private Rule findFirstResultByGroupId(RuleBizType ruleBizType, String currentGroupId, PbEvContext ctx) {
        String bizType = ruleBizType.getBizType();
        // 按一般性最大值50的六成初始化
        Map<String, Boolean> resTmp = new HashMap<>(30);
        RuleGroup ruleGroup = ruleBizType.getRuleGroup(bizType, currentGroupId);
        if (Objects.isNull(ruleGroup)) {
            LOGGER.warn("findFirstResultByGroupId: bizType[{}],groupId[{}] is not found", bizType, currentGroupId);
            return null;
        }
        return ruleGroup.getRuleList().stream().filter(rule -> {
            String resValExp = getRuleExp(ruleBizType, rule, ctx, resTmp);
            try {
                return PbElExpressionParser.getRuleRes(resValExp);
            } catch (Exception e) {
                LOGGER.error(APP_RULE_ENGINE_ERR_KEY + "bizType[{}],currentGroupId[{}],expStr[{}],err[{}]", bizType,
                        currentGroupId, rule.getExpStr(), e.getMessage());
                throw e;
            }
        }).findFirst().orElse(null);
    }
}
