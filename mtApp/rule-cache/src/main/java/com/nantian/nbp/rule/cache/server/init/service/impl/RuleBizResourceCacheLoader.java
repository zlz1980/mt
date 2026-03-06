/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.rule.cache.server.init.service.impl;

import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.rule.cache.server.init.mapper.rule.InitRuleBizMapper;
import com.nantian.nbp.rule.entity.Rule;
import com.nantian.nbp.rule.entity.RuleBizType;
import com.nantian.nbp.rule.entity.RuleCondition;
import com.nantian.nbp.rule.entity.RuleGroup;
import com.nantian.nbp.rule.entity.RuleGroupKey;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

public class RuleBizResourceCacheLoader extends ResourceCacheLoader<RuleBizType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleBizResourceCacheLoader.class);

    private final String tabSuffix;
    private final SqlSessionTemplate sqlSessionTemplate;

    public RuleBizResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate, String tabSuffix) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.tabSuffix = tabSuffix;
    }

    private void loadAllRuleCondition(PbResourceCache<RuleBizType> ruleBizCache) {
        List<RuleCondition> list = sqlSessionTemplate.getMapper(InitRuleBizMapper.class).findAllRuleCondList(tabSuffix);
        String recordBizType = null;
        String currentBizType;

        RuleBizType ruleBiz = null;
        for (RuleCondition ruleCondition : list) {
            currentBizType = ruleCondition.getBizType();
            // 按类型存放
            if (!Objects.equals(recordBizType, currentBizType)) {
                ruleBiz = ruleBizCache.get(currentBizType);
            }
            if (Objects.isNull(ruleBiz)) {
                throw new RuntimeException(String.format("RuleBizType not found, bizType:[%s]", currentBizType));
            }
            /* 计算RuleCondition的complexity */
            ruleCondition.initComplexity();
            ruleBiz.addRuleCondition(ruleCondition);
            recordBizType = currentBizType;
        }
    }

    private void loadAllRuleGroup(PbResourceCache<RuleBizType> ruleBizCache) {
        // 将规则集按业务类型及分组ID进行分组汇总，为了后续分别处理每组的规则集
        String recordBizType = null;
        String currentBizType;

        RuleGroupKey recordRuleGroupKey = null;
        RuleGroupKey currentRuleGroupKey;

        RuleBizType ruleBiz = null;
        RuleGroup ruleGroup = null;
        // 查询所有规则集
        List<Rule> ruleList = sqlSessionTemplate.getMapper(InitRuleBizMapper.class).findAllRuleList(tabSuffix);
        for (Rule rule : ruleList) {
            currentBizType = rule.getBizType();
            // 按类型存放
            if (!Objects.equals(recordBizType, currentBizType)) {
                ruleBiz = ruleBizCache.get(currentBizType);
            }
            if (Objects.isNull(ruleBiz)) {
                throw new RuntimeException(String.format("RuleBizType not found, bizType:[%s]", currentBizType));
            }
            currentRuleGroupKey = RuleGroupKey.of(currentBizType, rule.getGroupId());
            // 按类型，组存放
            if (!Objects.equals(recordRuleGroupKey, currentRuleGroupKey)) {
                ruleGroup = new RuleGroup(currentRuleGroupKey);
                ruleBiz.addRuleGroup(ruleGroup);
            }
            /* 计算并初始化Rule的complexity */
            rule.computeComplexity(ruleBiz);
            if (Objects.nonNull(ruleGroup)) {
                ruleGroup.add(rule);
            } else {
                throw new RuntimeException(APP_CACHE_ERR_KEY + "ruleGroup is null,RuleGroupKey:" + currentRuleGroupKey);
            }
            recordBizType = currentBizType;
            recordRuleGroupKey = currentRuleGroupKey;
        }
        // 遍历所有RuleGroup，基于其中的Rule的表达式复杂度进行Rule优先级排序
        for (RuleBizType currRuleBiz : ruleBizCache.values()) {
            for (RuleGroup ruleGroupEntry : currRuleBiz.getRuleGroupMap().values()) {
                // 对每个规则集进行处理，并重新排序：
                ruleGroupEntry.sortRuleListByRuleComplexity();
            }
        }
    }

    @Override
    public PbResourceCache<RuleBizType> initCache() {
        PbResourceCache<RuleBizType> ruleBizCache = new PbResourceCache<>(getResourceName(), 64);
        List<RuleBizType> ruleBizlist = sqlSessionTemplate.getMapper(InitRuleBizMapper.class)
                                                          .findAllRuleBizTypeList(tabSuffix);
        for (RuleBizType ruleBizType : ruleBizlist) {
            ruleBizCache.put(ruleBizType.getBizType(), ruleBizType);
        }
        /* 先加载并初始化RuleCondition，在初始化过程直接计算RuleCondition的complexity */
        loadAllRuleCondition(ruleBizCache);
        /* 依赖RuleCondition的complexity计算Rule的complexity */
        loadAllRuleGroup(ruleBizCache);

        return ruleBizCache;
    }

}
