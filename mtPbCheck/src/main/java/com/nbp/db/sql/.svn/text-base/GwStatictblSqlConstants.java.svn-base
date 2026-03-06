package com.nbp.db.sql;

public class GwStatictblSqlConstants {
    /** 查询网关静态缓存信息 */
    public static String GW_STATIC_TBL_SQL = "select * from T_PB_STATICTBL_GW";
    /** 查询网关静态缓存信息(根据工程id) */
    public static String GW_STATIC_TBL_SQL_WITH_PROID = "select * from T_PB_STATICTBL_GW t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'staticTblGw' and proid = ? and t1.crtproinfo = concat(t.cacheid,'_',t.keyname) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关静态缓存信息(根据缓存id) */
    public static String GW_STATIC_TBL_SQL_WITH_CACHEID = "select * from T_PB_STATICTBL_GW where cacheid =?";
}
