package com.nbp.db.sql;

/**
 * 导出内部交易相关的sql
 */
public class TranSqlConstants {
    /** 查询业务类型信息 */
    public static String BUSI_TYPE_SQL = "select * from T_PB_BUSI_TYPE";
    /** 查询业务类型 */
    public static String BUSITYPES_SQL = "select distinct BUSITYPE from T_PB_BUSI_TYPE";
    /** 查询内部交易码(根据业务类型) */
    public static String TRANCODES_SQL = "select distinct TRANCODE from T_PB_TRAN_CODE WHERE BUSITYPE = ?";
    /** 查询业务类型信息(根据工程id) */
    public static String BUSI_TYPE_SQL_WITH_PROID = "select * from T_PB_BUSI_TYPE t where exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询业务类型(根据工程id) */
    public static String BUSITYPES_SQL_WITH_PROID = "select distinct BUSITYPE from T_PB_TRAN_CODE t where exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.crtproinfo = t.trancode and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询内部交易码(根据工程id) */
    public static String TRANCODES_SQL_WITH_PROID = "select distinct TRANCODE from T_PB_TRAN_CODE t where BUSITYPE = ? and exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.crtproinfo = t.trancode and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询内部交易码信息(根据工程id) */
    public static String TRAN_CODE_SQL_WITH_PROID = "select * from T_PB_TRAN_CODE t where BUSITYPE = ? and tranCode = ? and exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.crtproinfo = t.trancode and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询流程信息(根据工程id) */
    public static String FLOW_SQL_WITH_PROID = "select * from T_PB_FLOW t where BUSITYPE = ? and tranCode = ? and exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.crtproinfo = t.trancode and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT')) order by t.busitype,t.trancode,t.atomtranno";
    /** 查询流程参数信息(根据工程id) */
    public static String FLOW_PARA_SQL_WITH_PROID = "select * from T_PB_FLOW_PARA t where BUSITYPE = ? and tranCode = ? and exists (select 1 from t_pb_project_info t1 where (CRTPROLV1 = 'Module' or CRTPROLV1 = 'TranCode') and proid = ? and t1.crtproinfo = t.trancode and t1.chnlno = t.BUSITYPE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询内部交易码信息(根据业务类型和内部交易码) */
    public static String TRAN_CODE_SQL = "select * from T_PB_TRAN_CODE where BUSITYPE = ? and tranCode = ?";
    /** 查询流程信息(根据业务类型和内部交易码) */
    public static String FLOW_SQL = "select * from T_PB_FLOW where BUSITYPE = ? and tranCode = ? order by busitype,trancode,atomtranno";
    /** 查询流程参数信息(根据业务类型和内部交易码) */
    public static String FLOW_PARA_SQL = "select * from T_PB_FLOW_PARA where BUSITYPE = ? and tranCode = ?";
}
