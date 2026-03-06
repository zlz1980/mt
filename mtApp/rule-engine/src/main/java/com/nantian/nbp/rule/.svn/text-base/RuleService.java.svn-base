package com.nantian.nbp.rule;

import com.nantian.nbp.ev.PbEvContext;
import com.nantian.nbp.rule.entity.Rule;

import java.util.Map;

/**
 * @author Administrator
 */
public interface RuleService {
    /**
     * 通过业务类型获取规则结果
     * @param bizType 业务种类
     * @param data 请求数据
     * @return Rule 结果值
     */
    Rule findRule(String bizType, Map<String, Object> data);

    /**
     * 通过业务类型获取规则结果
     * @param bizType 业务种类
     * @param data 请求数据
     * @return String 结果值
     */
    String findRuleResult(String bizType, Map<String, Object> data);

    /**
     * 通过业务类型获取规则结果
     * @param bizType 业务种类
     * @param evContext 请求数据
     * @return Rule 结果值
     */
    Rule findRule(String bizType, PbEvContext evContext);

    /**
     * 通过业务类型获取规则结果
     * @param bizType 业务种类
     * @param evContext 请求数据
     * @return String 结果值
     */
    String findRuleResult(String bizType, PbEvContext evContext);
}
