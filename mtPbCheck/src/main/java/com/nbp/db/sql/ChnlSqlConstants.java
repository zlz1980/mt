package com.nbp.db.sql;

/**
 * 导出外部交易相关sql
 */
public class ChnlSqlConstants {
    /** 查询外部交易渠道信息 */
    public static String CHNL_SQL = "select * from T_PB_CHNL";
    /** 查询外部交易渠道号 */
    public static String CHNLNOS_SQL = "select distinct CHNLNO from T_PB_CHNL";
    /** 查询外部交易码(根据渠道号) */
    public static String FTRANCODES_SQL = "select distinct FTRANCODE from T_PB_TRAN_PKG WHERE CHNLNO = ?";
    /** 查询外部交易渠道信息(根据工程id) */
    public static String CHNL_SQL_WITH_PROID = "select * from T_PB_CHNL t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'FTranCode' and proid = ? and t1.chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))";
    /** 查询外部交易码(根据工程id) */
    public static String CHNLNOS_SQL_WITH_PROID = "select distinct CHNLNO from T_PB_CHNL t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'FTranCode' and proid = ? and t1.chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))";
    /** 查询外部交易信息(根据工程id) */
    public static String TRAN_PKG_SQL_WITH_PROID = "select * from T_PB_TRAN_PKG t where CHNLNO = ? and FTRANCODE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'FTranCode' and proid = ? and t1.crtproinfo = t.FTRANCODE and t1.chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))";
    /** 查询外部交易映射信息(根据工程id) */
    public static String TRAN_CODE_CONV_SQL_WITH_PROID = "select * from T_PB_TRAN_CODE_CONV t where CHNLNO = ? and FTRANCODE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'FTranCode' and proid = ? and t1.crtproinfo = t.FTRANCODE and t1.chnlno = t.CHNLNO and (protype = 'NEW' or protype='EDIT'))";
    /** 查询外部交易信息(根据渠道号和外部交易码) */
    public static String TRAN_CODE_SQL_WITH_CHNLAN_AND_FTRANCODE = "select * from T_PB_TRAN_PKG where CHNLNO = ? and FTRANCODE = ?";
    /** 查询外部交易映射信息(根据渠道号和外部交易码) */
    public static String TRAN_CODE_CONV_SQL_WITH_CHNLAN_AND_FTRANCODE = "select * from T_PB_TRAN_CODE_CONV where CHNLNO = ? and FTRANCODE = ?";

}
