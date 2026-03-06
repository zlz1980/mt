package com.nbp.db.sql;

/**
 * 导出应用规则组相关sql
 */
public class RuleSqlConstants {
    /** 查询应用规则组规则类型 */
    public static String BIZTYPES_SQL = "select distinct bizType from T_PB_EXT_RULE_GROUP";
    /** 查询应用规则组规则类型(根据工程id) */
    public static String BIZTYPES_SQL_WITH_PROID = "select distinct bizType from T_PB_EXT_RULE_GROUP t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'ruleGroup' and proid = ? and t1.crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))"
            + " union "
            + " select chnlno from t_pb_project_info where proid = ? and (CRTPROLV1 = 'ruleSet' or CRTPROLV1 = 'rule')";
    /** 查询应用规则组信息(根据工程id) */
    public static String RULE_GROUP_SQL_WITH_PROID = "select * from T_PB_EXT_RULE_GROUP t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'ruleGroup' and proid = ? and t1.crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询应用规则组规则集信息(根据工程id) */
    public static String RULE_SET_SQL_WITH_PROID = "select * from T_PB_EXT_RULE_SET t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'ruleSet' and proid = ? and t1.crtproinfo = concat(t.bizType,'_',t.groupid,'_',t.ruleid) and t1.chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询应用规则组规则信息(根据工程id) */
    public static String RULE_SQL_WITH_PROID = "select * from T_PB_EXT_RULE t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'rule' and proid = ? and t1.crtproinfo = concat(t.bizType,'_',t.ruleName) and t1.chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询应用规则组信息(根据规则类型) */
    public static String RULE_GROUP_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE_GROUP where bizType = ?";
    /** 查询应用规则组规则集信息(根据规则类型) */
    public static String RULE_SET_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE_SET where bizType = ?";
    /** 查询应用规则组规则信息(根据规则类型) */
    public static String RULE_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE where bizType = ?";

}
