package com.nbp.db.sql;

/**
 * 导出原子交易相关sql
 */
public class AtomSqlConstants {
    /** 查询原子交易目录信息 */
    public static String STREAM_SQL = "select * from T_PB_CTRL_STREAM";
    /** 查询原子交易目录号 */
    public static String STREAMCODES_SQL = "select distinct STREAMCODE from T_PB_ATOM_TRAN";
    /** 查询原子交易码 */
    public static String ATOMCODES_SQL = "select distinct ATOMTRANCODE from T_PB_ATOM_TRAN where STREAMCODE = ?";
    /** 查询原子交易信息 */
    public static String ATOM_SQL = "select * from T_PB_ATOM_TRAN where STREAMCODE = ? and ATOMTRANCODE = ?";
    /** 查询原子交易目录信息(根据工程id) */
    public static String STREAM_SQL_WITH_PROID = "select * from T_PB_CTRL_STREAM t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'AtomTran' and proid = ? and t1.chnlno = t.STREAMCODE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询原子交易目录号(根据工程id) */
    public static String STREAMCODES_SQL_WITH_PROID = "select distinct STREAMCODE from T_PB_ATOM_TRAN t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'AtomTran' and proid = ? and t1.crtproinfo = t.ATOMTRANCODE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询原子交易信息(根据工程id) */
    public static String ATOM_SQL_WITH_PROID = "select * from T_PB_ATOM_TRAN t where STREAMCODE = ? and ATOMTRANCODE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'AtomTran' and proid = ? and t1.crtproinfo = t.ATOMTRANCODE and (protype = 'NEW' or protype='EDIT'))";
    /** 查询原子交易目录号(根据原子交易码) */
    public static String STREAMCODES_SQL_WITH_ATOMTRANCODE = "select distinct STREAMCODE from T_PB_ATOM_TRAN where ATOMTRANCODE = ?";

}
