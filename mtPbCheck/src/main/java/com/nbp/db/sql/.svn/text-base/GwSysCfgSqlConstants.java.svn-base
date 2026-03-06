package com.nbp.db.sql;

public class GwSysCfgSqlConstants {
    /** 查询网关系统配置信息 */
    public static String GW_SYS_CFG_SQL = "select * from T_PB_SYS_CFG_GW";
    /** 查询网关系统配置信息(根据工程id) */
    public static String GW_SYS_CFG_SQL_WITH_PROID = "select * from T_PB_SYS_CFG_GW t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'sysCfgGw' and proid = ? and t1.crtproinfo = t.DEFNAME and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关系统配置信息(根据配置名称) */
    public static String GW_SYS_CFG_SQL_WITH_DEFNAME = "select * from T_PB_SYS_CFG_GW where DEFNAME = ?";
}
