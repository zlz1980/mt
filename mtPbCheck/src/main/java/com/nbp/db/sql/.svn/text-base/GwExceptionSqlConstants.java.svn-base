package com.nbp.db.sql;

/**
 * 查询网关异常处理数据相关的sql
 */
public class GwExceptionSqlConstants {
    /** 查询网关异常处理渠道号 */
    public static String GW_EXCEPTION_REQCHNLS_SQL = "select distinct REQCHNL from T_PB_EXCEPTION_GW";
    /** 查询网关异常处理渠道号下的code */
    public static String GW_EXCEPTION_CODES_SQL = "select distinct CODE from T_PB_EXCEPTION_GW where REQCHNL = ?";
    /** 查询网关异常处理渠道号(根据工程id) */
    public static String GW_EXCEPTION_REQCHNLS_SQL_WITH_PROID = "select distinct REQCHNL from T_PB_EXCEPTION_GW t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'exceptionGw' and proid = ? and t1.crtproinfo = CONCAT(t.reqChnl,'_',t.code) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关异常处理渠道号下的code(根据工程id) */
    public static String GW_EXCEPTION_CODES_SQL_WITH_PROID = "select distinct CODE from T_PB_EXCEPTION_GW t where REQCHNL = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'exceptionGw' and proid = ? and t1.crtproinfo = CONCAT(t.reqChnl,'_',t.code) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关异常处理信息(根据工程id) */
    public static String GW_EXCEPTION_SQL_WITH_PROID = "select * from T_PB_EXCEPTION_GW t where REQCHNL = ? AND CODE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'exceptionGw' and proid = ? and t1.crtproinfo = CONCAT(t.reqChnl,'_',t.code) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关异常处理信息 */
    public static String GW_EXCEPTION_SQL_WITH_REQCHNL_AND_CODE = "select * from T_PB_EXCEPTION_GW where REQCHNL = ? AND CODE = ?";
}
