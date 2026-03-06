package com.nbp.db.sql;

/**
 * 导出组件目录数据
 */
public class ModuleSqlConstants {
    /** 查询组件目录信息 */
    public static String MODULE_DIR_SQL = "select * from T_PB_EXT_MODULE_DIR";
    /** 查询组件目录信息(根据工程id) */
    public static String MODULE_DIR_SQL_WITH_PROID = "select * from T_PB_EXT_MODULE_DIR t where exists (select 1 from t_pb_project_dir t1 where (PROTYPE = 'Module' or PROTYPE = 'Module_000' or PROTYPE = 'Module_INF') and proid = ? and t1.DIRCODE = t.TYPE and (STYLE = 'NEW' or STYLE='EDIT'))";
    /** 查询组件目录信息(根据目录类型) */
    public static String MODULE_DIR_SQL_WITH_TYPE = "select * from T_PB_EXT_MODULE_DIR where TYPE = ?";
    /** 查询组件目录与组件关系信息(根据组件交易码) */
    public static String MODULE_SQL_WITH_TRANCODE = "select * from T_PB_EXT_MODULE where TRANCODE = ?";
}
