package com.nbp.db.sql;

public class GwRuleSqlConstants {
    /** 查询网关规则组规则类型 */
    public static String GW_BIZTYPES_SQL = "select distinct bizType from T_PB_EXT_RULE_GROUP_GW";
    /** 查询网关规则组规则类型(根据工程id) */
    public static String GW_BIZTYPES_SQL_WITH_PROID = "select distinct bizType from T_PB_EXT_RULE_GROUP_GW t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'gwRuleGroup' and proid = ? and t1.crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))"
            + " union "
            + " select chnlno from t_pb_project_info where proid = ? and (CRTPROLV1 = 'gwRuleSet' or CRTPROLV1 = 'gwRule')";
    /** 查询网关规则组信息(根据工程id) */
    public static String GW_RULE_GROUP_SQL_WITH_PROID = "select * from T_PB_EXT_RULE_GROUP_GW t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'gwRuleGroup' and proid = ? and t1.crtproinfo = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关规则组规则集信息(根据工程id) */
    public static String GW_RULE_SET_SQL_WITH_PROID = "select * from T_PB_EXT_RULE_SET_GW t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'gwRuleSet' and proid = ? and t1.crtproinfo = concat(t.bizType,'_',t.groupid,'_',t.ruleid) and t1.chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关规则组规则信息(根据工程id) */
    public static String GW_RULE_SQL_WITH_PROID = "select * from T_PB_EXT_RULE_GW t where bizType = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'gwRule' and proid = ? and t1.crtproinfo = concat(t.bizType,'_',t.ruleName) and t1.chnlno = t.bizType and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关规则组信息(根据规则类型) */
    public static String GW_RULE_GROUP_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE_GROUP_GW where bizType = ?";
    /** 查询网关规则组规则集信息(根据规则类型) */
    public static String GW_RULE_SET_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE_SET_GW where bizType = ?";
    /** 查询网关规则组规则信息(根据规则类型) */
    public static String GW_RULE_SQL_WITH_BIZTYPE = "select * from T_PB_EXT_RULE_GW where bizType = ?";
}
