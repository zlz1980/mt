package com.nbp.db.sql;

/**
 * 导出sql配置相关的sql
 */
public class SqlTemplateSqlConstants {
    /** 查询sql配置信息 */
    public static String TEMPLATE_SQL = "select * from T_PB_EXT_SQL_TEMPLATE";
    /** 查询sql配置信息(根据工程id) */
    public static String TEMPLATE_SQL_WITH_PROID = "select * from T_PB_EXT_SQL_TEMPLATE t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'sqlTemplate' and proid = ? and t1.crtproinfo = t.sqlName and (protype = 'NEW' or protype='EDIT'))";
    /** 查询sql配置信息(根据sql名称) */
    public static String TEMPLATE_SQL_WITH_SQLNAME = "select * from T_PB_EXT_SQL_TEMPLATE where sqlName =?";
}
