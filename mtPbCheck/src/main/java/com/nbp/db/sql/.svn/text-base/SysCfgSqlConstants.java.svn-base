package com.nbp.db.sql;

/**
 * 导出应用系统配置相关的sql
 */
public class SysCfgSqlConstants {
    /** 查询系统配置信息 */
    public static String SYS_CFG_SQL = "select * from T_PB_SYS_CFG";
    /** 查询系统配置信息(根据工程id) */
    public static String SYS_CFG_SQL_WITH_PROID = "select * from T_PB_SYS_CFG t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'sysCfg' and proid = ? and t1.crtproinfo = t.DEFNAME and (protype = 'NEW' or protype='EDIT'))";
    /** 查询系统配置信息(根据配置名称) */
    public static String SYS_CFG_SQL_WITH_DEFNAME = "select * from T_PB_SYS_CFG where DEFNAME = ?";
}
