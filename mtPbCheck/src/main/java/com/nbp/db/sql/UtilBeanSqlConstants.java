package com.nbp.db.sql;

/**
 * 导出工具bean相关的sql
 */
public class UtilBeanSqlConstants {
    /** 查询工具类信息 */
    public static String UTILBEAN_SQL = "select * from t_pb_ext_utils_bean";
    /** 查询工具类信息(根据工程id) */
    public static String UTILBEAN_SQL_WITH_PROID = "select * from t_pb_ext_utils_bean t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'utilBean' and proid = ? and t1.crtproinfo = t.beanName and (protype = 'NEW' or protype='EDIT'))";
    /** 查询工具类信息(根据beanName) */
    public static String UTILBEAN_SQL_WITH_BEANNAME = "select * from t_pb_ext_utils_bean where beanName =?";
}
