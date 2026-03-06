package com.nbp.db.sql;

/**
 * 导出静态缓存相关的sql
 */
public class StatictblSqlConstants {
    /** 查询静态缓存信息 */
    public static String STATIC_TBL_SQL = "select * from T_PB_STATICTBL_APP";
    /** 查询静态缓存信息(根据工程id) */
    public static String STATIC_TBL_SQL_WITH_PROID = "select * from T_PB_STATICTBL_APP t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'staticTbl' and proid = ? and t1.crtproinfo = concat(t.cacheid,'_',t.keyname) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询静态缓存信息(根据缓存id) */
    public static String STATIC_TBL_SQL_WITH_CACHEID = "select * from T_PB_STATICTBL_APP where cacheid =?";
}
