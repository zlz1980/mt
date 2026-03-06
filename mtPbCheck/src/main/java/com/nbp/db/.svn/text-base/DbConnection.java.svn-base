package com.nbp.db;

import com.nbp.util.CheckingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nbp.env.EnvVar.envPASSWORD;
import static com.nbp.env.EnvVar.envURL;
import static com.nbp.env.EnvVar.envUSER;
import static com.nbp.env.EnvVar.envDbDriver;
import static com.nbp.util.Constants.BASEMSG;
import static com.nbp.util.Constants.CONDMSG;
import static com.nbp.util.Constants.DATAMSG;
import static com.nbp.util.Constants.DATA_SPLIT_FLAG;
import static com.nbp.util.Constants.FLDSMSG;
import static com.nbp.util.Constants.LINE_SEPARATOR;
import static com.nbp.util.Constants.LINE_SEPARATOR2;
import static com.nbp.util.Constants.LINE_SEPARATOR_REPLACE;
import static com.nbp.util.Constants.LINE_SEPARATOR_REPLACE2;
import static com.nbp.util.Constants.SPLIT_FLAG;
import static com.nbp.db.sql.MysqlSqlConstants.SELECT_PRIS_SQL_MYSQL;

/**
 * 数据库操作类
 */
public class DbConnection {
    private static final Logger logger = LoggerFactory.getLogger(DbConnection.class);

    /**
     * 获取数据库连接
     * @return 数据库连接
     */
    public static Connection getConn() {
        try {
            Class.forName(envDbDriver);
            return DriverManager.getConnection(envURL, envUSER, envPASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("获取数据库连接失败:",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行count语句
     * @param conn 数据库连接
     * @param sql count语句
     * @param params 参数
     * @return 查询结果
     */
    public static int executeQueryCountSql(Connection conn, String sql, Object... params){
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            if (params.length != 0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1,params[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("sql={}",sql);
            logger.error("params={}",params);
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 查询一列值
     * @param conn 数据库连接
     * @param sql sql语句
     * @param params 参数
     * @return 返回第一列值的集合
     */
    public static List<String> excuteQueryList(Connection conn, String sql, Object... params){
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            if (params.length != 0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1,params[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()){
                    return result;
                }
                do {
                    result.add(rs.getString(1));
                } while(rs.next());
            }
        } catch (SQLException e) {
            logger.debug("sql={}",sql);
            logger.debug("params={}",params);
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 查询
     * @param conn 数据库连接
     * @param sql sql语句
     * @param params 参数 字段名，值，字段名，值...
     * @return 返回String形式的查询结果
     * 表名        BASEMSG|表名
     * 查询字段    CONDMSG|字段1|字段2...
     * 字段信息    FLDSMSG|字段1|字段2...
     * 值信息    DATAMSG|+|值1|+|值2...
     */
    public static String executeQuery(Connection conn, String sql, Object... params){
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            if (params.length != 0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1,params[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()){
                    return "";
                }
                String tableName = rs.getMetaData().getTableName(1);
                //获取表名信息
                String baseMsg = BASEMSG + SPLIT_FLAG + tableName + LINE_SEPARATOR;
                int columnCount = rs.getMetaData().getColumnCount();
                //获取字段信息
                StringBuilder fldsMsg = new StringBuilder(FLDSMSG);
                for (int i = 1;i <= columnCount;i++) {
                    fldsMsg.append(SPLIT_FLAG);
                    fldsMsg.append(rs.getMetaData().getColumnName(i));
                }
                fldsMsg.append(LINE_SEPARATOR);
                StringBuilder dataMag = new StringBuilder();
                int dataCount = 0;
                do {
                    dataCount++;
                    //获取值信息
                    dataMag.append(DATAMSG);
                    for(int i = 1;i <= columnCount;i++) {
                        dataMag.append(DATA_SPLIT_FLAG);
                        String s = rs.getString(i);
                        if (null != s) {
                            //对单个数据进行校验，不允许使用导出文件中的转义符与分隔符
                            if(s.contains(DATA_SPLIT_FLAG)
                                || s.contains(LINE_SEPARATOR_REPLACE2)
                                || s.contains(LINE_SEPARATOR_REPLACE))
                            {
                                logger.error("数据表{}第{}列中存在非法字符{}，请检查数据是否符合要求",tableName,i,s);
                                throw new RuntimeException("数据中包含非法字符，请检查数据是否符合要求");
                            }
                            s = s.replace(LINE_SEPARATOR2,LINE_SEPARATOR_REPLACE2)
                                .replace(LINE_SEPARATOR,LINE_SEPARATOR_REPLACE);
                        }
                        dataMag.append(s);
                    }
                    dataMag.append(LINE_SEPARATOR);
                } while(rs.next());
                dataMag.append("####################[表");
                dataMag.append(tableName);
                dataMag.append("加载完成共");
                dataMag.append(dataCount);
                dataMag.append("条数据]####################");
                dataMag.append(LINE_SEPARATOR);
                sb.append(baseMsg);
                sb.append(executeQueryTableKey(conn, tableName));
                sb.append(fldsMsg);
                sb.append(dataMag);
                CheckingUtils.putTableNum(tableName.toUpperCase(), dataCount);
            }
        } catch (SQLException e) {
            logger.error("sql={}",sql);
            logger.error("params={}", Arrays.toString(params));
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 查询mysql表的主键字段信息
     * @param conn 数据库连接
     * @param tableName 表名
     * @return 表主键字段 CONDMSG|字段1|字段2...
     * @throws SQLException sql执行异常
     */
    private static StringBuilder executeQueryTableKey (Connection conn, String tableName) throws SQLException {
         try (PreparedStatement ps = conn.prepareStatement(SELECT_PRIS_SQL_MYSQL)){
             StringBuilder condMsg = new StringBuilder(CONDMSG);
             //MySql获取表的主键字段信息
             ps.setString(1, tableName);
             ps.executeQuery();
             try (ResultSet rs = ps.getResultSet()) {
                 if (rs.next()) {
                     do {
                         condMsg.append(SPLIT_FLAG);
                         condMsg.append(rs.getString(1));
                     } while (rs.next());
                 } else {
                     //获取主键字段失败
                     logger.error("获取主键字段失败，请检查表{}是否存在主键", tableName);
                     throw new RuntimeException("获取主键字段失败，请检查表是否存在主键");
                 }
                 condMsg.append(LINE_SEPARATOR);
             }
             return condMsg;
         }
    }

        /**
     * 新增
     * @param sqls sql语句集合
     * @return insert语句影响行数
     */
    public static int executeUpdate(Connection conn, List<String> sqls){
        int result = 0;
        StringBuilder errMsg = new StringBuilder();
        try {
            // 关闭自动提交
            conn.setAutoCommit(false);
            for (String sql : sqls) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    //只统计insert影响行数
                    if (sql.contains("insert")) {
                        result += ps.executeUpdate();
                    } else {
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    errMsg.append("execute sql:");
                    errMsg.append(sql);
                    errMsg.append(" error:");
                    errMsg.append(e.getMessage());
                    errMsg.append(LINE_SEPARATOR);
                }
            }
            if (errMsg.length() == 0) {
                conn.commit();
            } else {
                conn.rollback();
                logger.error(errMsg.toString());
                throw new RuntimeException("执行sql失败，请查看日志并检查文件数据");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    public static int executeUpdate(Connection conn, String sql, String... params){
        int result;
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            if (params.length != 0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1,params[i]);
                }
            }
            result = ps.executeUpdate();
        } catch (SQLException e) {
            logger.debug("sql={}",sql);
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void executeDDL(Connection conn, String sql, String... params){
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            if (params.length != 0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1,params[i]);
                }
            }
            ps.execute();
        } catch (SQLException e) {
            logger.debug("sql={}",sql);
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
