package com.nbp.db.sql;

/**
 * 导出接口规范相关的sql
 */
public class ExtApiSqlConstants {
    /** 查询业务类型 */
    public static String BUSITYPES_SQL = "select distinct BUSITYPE from T_PB_EXT_API";
    /** 查询接口规范id(根据业务类型) */
    public static String APICODES_SQL = "select distinct APICODE from T_PB_EXT_API WHERE BUSITYPE = ?";
    /** 查询业务类型(根据工程id) */
    public static String BUSITYPES_SQL_WITH_PROID = "select distinct BUSITYPE from T_PB_EXT_API t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'extApi' and proid = ? and t1.crtproinfo = concat(t.BUSITYPE,concat('_',t.apicode)) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询接口规范id(根据工程id) */
    public static String APICODES_SQL_WITH_PROID = "select distinct APICODE from T_PB_EXT_API t where BUSITYPE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'extApi' and proid = ? and t1.crtproinfo = concat(t.BUSITYPE,concat('_',t.apicode)) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询接口规范信息(根据工程id) */
    public static String EXT_API_SQL_WITH_PROID = "select * from T_PB_EXT_API t where BUSITYPE = ? and apiCode = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'extApi' and proid = ? and t1.crtproinfo = concat(t.BUSITYPE,concat('_',t.apicode)) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询接口规范信息(根据业务类型和接口规范id) */
    public static String EXT_API_SQL = "select * from T_PB_EXT_API where BUSITYPE = ? and apiCode = ?";
}
