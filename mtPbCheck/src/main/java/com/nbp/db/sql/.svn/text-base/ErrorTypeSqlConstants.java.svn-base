package com.nbp.db.sql;

/**
 * 导出错误码相关sql
 */
public class ErrorTypeSqlConstants {
    /** 查询错误码信息 */
    public static String ERRORTYPE_SQL = "select * from T_PB_EXT_ERROR_TYPE";
    /** 查询错误码信息(根据工程id) */
    public static String ERRORTYPE_SQL_WITH_PROID = "select * from T_PB_EXT_ERROR_TYPE t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'errorType' and proid = ? and t1.crtproinfo = t.typeid and (protype = 'NEW' or protype='EDIT'))";
    /** 查询错误码信息(根据错误码) */
    public static String ERRORTYPE_SQL_WITH_ID = "select * from T_PB_EXT_ERROR_TYPE where typeid =?";
}
