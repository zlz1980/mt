package com.nbp.db.sql;

/**
 * MySql查询主键语句
 */
public class MysqlSqlConstants {
//    public static final String SELECT_PRIS_SQL_MYSQL = "select COLUMN_NAME from information_schema.columns where TABLE_SCHEMA = DATABASE() and TABLE_NAME = ? and COLUMN_KEY = 'PRI' order by COLUMN_NAME";
    public static final String SELECT_PRIS_SQL_MYSQL = "SELECT C.COLUMN_NAME\n" +
        "FROM USER_TAB_COLUMNS C\n" +
        "JOIN USER_CONS_COLUMNS CC ON C.COLUMN_NAME = CC.COLUMN_NAME AND C.TABLE_NAME = CC.TABLE_NAME\n" +
        "JOIN USER_CONSTRAINTS CT ON CC.CONSTRAINT_NAME = CT.CONSTRAINT_NAME AND CT.CONSTRAINT_TYPE = 'P'\n" +
        "WHERE C.TABLE_NAME = ?\n" +
        "ORDER BY C.COLUMN_NAME;";
}
