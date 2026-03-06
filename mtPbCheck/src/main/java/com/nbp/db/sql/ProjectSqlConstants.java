package com.nbp.db.sql;

/**
 * 导出工程信息相关sql
 */
public class ProjectSqlConstants {
    /** 查询所有工程id */
    public static String PROIDS_SQL = "SELECT PROID from T_PB_PROJECT";
    /** 查询工程信息 */
    public static String PROJECT_SQL = "select * from T_PB_PROJECT";
    /** 查询工程目录信息 */
    public static String PROJECT_DIR_SQL = "select * from T_PB_PROJECT_DIR";
    /** 查询工程资源信息 */
    public static String PROJECT_INFO_SQL = "select * from T_PB_PROJECT_INFO";
    /** 查询工程信息(根据工程id) */
    public static String PROJECT_SQL_WITH_PROID = "select * from T_PB_PROJECT where proid = ?";
    /** 查询工程目录信息(根据工程id) */
    public static String PROJECT_DIR_SQL_WITH_PROID = "select * from T_PB_PROJECT_DIR where proid = ?";
    /** 查询工程资源信息(根据工程id) */
    public static String PROJECT_INFO_SQL_WITH_PROID = "select * from T_PB_PROJECT_INFO where proid = ?";
}
