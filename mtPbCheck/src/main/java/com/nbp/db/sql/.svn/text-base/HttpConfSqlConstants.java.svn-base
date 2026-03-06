package com.nbp.db.sql;

/**
 * 导出http通讯相关sql
 */
public class HttpConfSqlConstants {
    /** 查询HTTP通讯信息 */
    public static String HTTP_SQL = "select * from T_PB_EXT_HTTP_CONF";
    /** 查询HTTP通讯信息(根据工程id) */
    public static String HTTP_SQL_WITH_PROID = "select * from T_PB_EXT_HTTP_CONF t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'httpConf' and proid = ? and t1.crtproinfo = t.serName and (protype = 'NEW' or protype='EDIT'))";
    /** 查询HTTP通讯信息(根据服务名称) */
    public static String HTTP_SQL_WITH_SERNAME = "select * from T_PB_EXT_HTTP_CONF where serName =?";
}
