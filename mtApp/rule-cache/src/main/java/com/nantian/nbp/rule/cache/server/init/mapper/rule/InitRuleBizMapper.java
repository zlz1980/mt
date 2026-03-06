package com.nantian.nbp.rule.cache.server.init.mapper.rule;

import com.nantian.nbp.rule.entity.RuleCondition;
import com.nantian.nbp.rule.entity.RuleBizType;
import com.nantian.nbp.rule.entity.Rule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Administrator
 */
public interface InitRuleBizMapper {

    @Select("SELECT m.bizType,m.groupIdExp FROM T_PB_EXT_RULE_GROUP${tabSuffix} m ")
    List<RuleBizType> findAllRuleBizTypeList(@Param("tabSuffix") String tabSuffix);

    @Select("SELECT m.bizType,m.groupId,m.ruleId,m.expStr,m.result FROM T_PB_EXT_RULE_SET${tabSuffix} m order by m.bizType,m.groupId, m.ruleId")
    List<Rule> findAllRuleList(@Param("tabSuffix") String tabSuffix);

    @Select("SELECT m.bizType,m.ruleName as condName, m.ruleExp as condExp FROM T_PB_EXT_RULE${tabSuffix} m order by m.bizType")
    List<RuleCondition> findAllRuleCondList(@Param("tabSuffix") String tabSuffix);
}
