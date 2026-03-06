package com.nbp.db.sql;

public class GwTranRouteSqlConstants {
    /** 查询网关路由控制渠道号 */
    public static String GW_TRANROUTE_REQCHNLS_SQL = "select distinct REQCHNL from T_PB_TRANROUTE_GW";
    /** 查询网关路由控制外部交易码 */
    public static String GW_TRANROUTE_FTRANCODES_SQL = "select distinct FTRANCODE from T_PB_TRANROUTE_GW where REQCHNL = ?";
    /** 查询网关路由控制渠道号(根据工程id) */
    public static String GW_TRANROUTE_REQCHNLS_SQL_WITH_PROID = "select distinct REQCHNL from T_PB_TRANROUTE_GW t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'tranRouteGw' and proid = ? and t1.crtproinfo = concat(t.REQCHNL,'_',t.FTRANCODE) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关路由控制外部交易码(根据工程id) */
    public static String GW_TRANROUTE_FTRANCODES_SQL_WITH_PROID = "select distinct FTRANCODE from T_PB_TRANROUTE_GW t where REQCHNL = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'tranRouteGw' and proid = ? and t1.crtproinfo = concat(t.REQCHNL,'_',t.FTRANCODE) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关路由控制信息(根据工程id) */
    public static String GW_TRANROUTE_SQL_WITH_PROID = "select * from T_PB_TRANROUTE_GW t where REQCHNL = ? AND FTRANCODE = ? and exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'tranRouteGw' and proid = ? and t1.crtproinfo = concat(t.REQCHNL,'_',t.FTRANCODE) and (protype = 'NEW' or protype='EDIT'))";
    /** 查询网关路由控制信息(根据渠道号和外部交易码) */
    public static String GW_TRANROUTE_SQL_WITH_REQCHNL_AND_FTRANCODE = "select * from T_PB_TRANROUTE_GW where REQCHNL = ? AND FTRANCODE = ?";
}
