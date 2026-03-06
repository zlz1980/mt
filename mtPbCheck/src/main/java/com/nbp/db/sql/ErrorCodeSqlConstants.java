package com.nbp.db.sql;

/**
 * 导出错误码相关sql
 */
public class ErrorCodeSqlConstants {
    /** 查询错误码信息 */
    public static String ERRORCODE_SQL = "select * from T_PB_EXT_ERROR_CODE";
    /** 查询错误码信息(根据工程id) */
    public static String ERRORCODE_SQL_WITH_PROID = "select * from T_PB_EXT_ERROR_CODE t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'ErrorCode' and proid = ? and t1.crtproinfo = t.errorcode and (protype = 'NEW' or protype='EDIT'))";
    /** 查询错误码信息(根据错误码) */
    public static String ERRORCODE_SQL_WITH_ERRORCODE = "select * from T_PB_EXT_ERROR_CODE where errorcode =?";
}
