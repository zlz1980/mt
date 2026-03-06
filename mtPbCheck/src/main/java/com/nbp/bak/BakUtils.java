package com.nbp.bak;

import com.nbp.db.DbConnection;
import com.nbp.util.VersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import static com.nbp.util.Constants.BAK_COMMIT_SIZE;

/**
 * 备份表相关工具类
 */
public class BakUtils {

    private static final Logger logger = LoggerFactory.getLogger(BakUtils.class);

    /**
     * 应用相关的表
     */
    private static final String [] APP_TABLE_NAMES;
    /**
     * 网关相关的表
     */
    private static final String [] GW_TABLE_NAMES;

    static {
        APP_TABLE_NAMES = new String[]{
            "T_PB_BATCH",
            "T_PB_SYS_CFG",
            "T_PB_EXT_UTILS_BEAN",
            "T_PB_CTRL_STREAM",
            "T_PB_ATOM_TRAN",
            "T_PB_EXT_RULE_GROUP",
            "T_PB_EXT_RULE_SET",
            "T_PB_EXT_RULE",
            "T_PB_EXT_SQL_TEMPLATE",
            "T_PB_STATICTBL_APP",
            "T_PB_EXT_ERROR_CODE",
            "T_PB_EXT_HTTP_CONF",
            "T_PB_BUSI_TYPE",
            "T_PB_TRAN_CODE",
            "T_PB_FLOW",
            "T_PB_FLOW_PARA",
            "T_PB_EXT_API",
            "T_PB_CHNL",
            "T_PB_TRAN_PKG",
            "T_PB_TRAN_CODE_CONV",
            "T_PB_PROJECT_DIR",
            "T_PB_PROJECT",
            "T_PB_PROJECT_INFO"
        };
        GW_TABLE_NAMES = new String[]{
            "T_PB_STATICTBL_GW",
            "T_PB_EXT_RULE_GROUP_GW",
            "T_PB_EXT_RULE_SET_GW",
            "T_PB_EXT_RULE_GW",
            "T_PB_SYS_CFG_GW",
            "T_PB_TESTROUTE_GW",
            "T_PB_TRANROUTE_GW",
            "T_PB_EXCEPTION_GW"
        };
    }

    /**
     * 备份pb相关的所有表
     * @param conn 数据库连接
     */
    public static void bakAll(Connection conn) {
        //获取当前版本号
        String version = VersionUtils.getVersion(conn);
        //获取当前网关版本号
        String gwVersion = VersionUtils.getGWVersion(conn);
        logger.info("开始备份应用数据,当前版本号:{}",version);
        for (String tableName : APP_TABLE_NAMES) {
            String bakTableName;
            if (version == null || version.isEmpty()) {
                bakTableName = tableName + "_bak";
            } else {
                bakTableName = tableName + "_" + version;
            }
            drop(conn, bakTableName);
            bakMySql(conn, bakTableName ,tableName);
        }
        logger.info("备份应用数据结束");
        logger.info("开始备份网关数据,当前版本号:{}",version);
        for (String tableName : GW_TABLE_NAMES) {
            String bakTableName;
            if (gwVersion == null || gwVersion.isEmpty()) {
                bakTableName = tableName + "_bak";
            } else {
                bakTableName = tableName + "_" + gwVersion;
            }
            drop(conn, bakTableName);
            bakMySql(conn, bakTableName ,tableName);
        }
        logger.info("备份网关数据结束");
    }

    /**
     * 备份表,mysql格式语句
     * @param conn 数据库连接
     * @param bakTableName 备份表名称
     * @param tableName 原始表名称
     */
    private static void bakMySql(Connection conn, String bakTableName, String tableName) {
        String createSql = "create table " + bakTableName +
                " like " +
                tableName;
        String countSql = "select count(*) from " + tableName;
        int count = DbConnection.executeQueryCountSql(conn, countSql);
        DbConnection.executeDDL(conn, createSql);
        for (int i = 0; i < count / BAK_COMMIT_SIZE + 1; i++) {
            String insertSql = "insert into " + bakTableName +
                    " select * from " +
                    tableName + " limit " + BAK_COMMIT_SIZE + " offset " + i*BAK_COMMIT_SIZE;
            DbConnection.executeUpdate(conn, insertSql);
        }
    }

    /**
     * 删除表，用于备份时已存在同名的备份表信息
     * @param conn 数据库连接
     * @param tableName 表名称
     */
    private static void drop(Connection conn, String tableName) {
        DbConnection.executeDDL(conn, "drop table IF EXISTS " + tableName);
    }

}
