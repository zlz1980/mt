package com.nbp.db.sql;

/**
 * 导出批量任务相关sql
 */
public class BatchSqlConstants {
    /** 查询批量交易信息 */
    public static String BATCH_SQL = "select * from T_PB_BATCH";
    /** 查询批量交易信息(根据工程id) */
    public static String BATCH_SQL_WITH_PROID = "select * from T_PB_BATCH t where exists (select 1 from t_pb_project_info t1 where CRTPROLV1 = 'batch' and proid = ? and t1.crtproinfo = t.taskName and (protype = 'NEW' or protype='EDIT'))";
    /** 查询批量交易信息(根据任务名称) */
    public static String BATCH_SQL_WITH_TASKNAME = "select * from T_PB_BATCH where taskName = ?";
}
