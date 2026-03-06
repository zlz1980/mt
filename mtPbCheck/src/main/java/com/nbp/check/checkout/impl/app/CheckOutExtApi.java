package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import static com.nbp.db.sql.ExtApiSqlConstants.APICODES_SQL;
import static com.nbp.db.sql.ExtApiSqlConstants.APICODES_SQL_WITH_PROID;
import static com.nbp.db.sql.ExtApiSqlConstants.BUSITYPES_SQL;
import static com.nbp.db.sql.ExtApiSqlConstants.BUSITYPES_SQL_WITH_PROID;
import static com.nbp.db.sql.ExtApiSqlConstants.EXT_API_SQL;
import static com.nbp.db.sql.ExtApiSqlConstants.EXT_API_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.EXTAPI_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出接口规范类
 */
public class CheckOutExtApi extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutExtApi.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始执行导出接口规范数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + EXTAPI_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else if (ALL_REGEX.equals(params2)) {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        } else {
            executorCheckOutByParam2(conn, params1, params2, infileParentPath);
        }
        logger.info("导出接口规范数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有接口规范数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        List<String> dbBusiTypes = DbConnection.excuteQueryList(conn, BUSITYPES_SQL);
        for (String dbBusiType : dbBusiTypes) {
            List<String> dbApiCodes = DbConnection.excuteQueryList(conn, APICODES_SQL, dbBusiType);
            for (String dbApiCode : dbApiCodes) {
                String apiData = DbConnection.executeQuery(conn, EXT_API_SQL, dbBusiType, dbApiCode);
                if (apiData.isEmpty()) {
                    logger.error("数据库中没有对应的接口规范数据");
                } else {
                    FileUtils.writeFile(apiData, infileParentPath
                            + dbBusiType + File.separator + dbApiCode + ".data");
                }
            }
        }
    }
    /**
     * 导出指定工程id的接口规范数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        List<String> dbBusiTypes = DbConnection.excuteQueryList(conn, BUSITYPES_SQL_WITH_PROID, proIds);
        for (String dbBusiType : dbBusiTypes) {
            List<String> dbApiCodes = DbConnection.excuteQueryList(conn, APICODES_SQL_WITH_PROID, dbBusiType, proIds);
            for (String dbApiCode : dbApiCodes) {
                String apiData = DbConnection.executeQuery(conn, EXT_API_SQL_WITH_PROID, dbBusiType, dbApiCode, proIds);
                if (apiData.isEmpty()) {
                    logger.error("数据库中没有对应的接口规范数据");
                } else {
                    FileUtils.writeFile(apiData, infileParentPath
                            + dbBusiType + File.separator + dbApiCode + ".data");
                }
            }
        }
    }
    /**
     * 导出指定业务种类下的接口规范数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] bizTypeArr = params1.split(SPLIT_REGEX);
        for (String bizType : bizTypeArr) {
            List<String> dbApiCodes = DbConnection.excuteQueryList(conn, APICODES_SQL, bizType);
            for (String dbApiCode : dbApiCodes) {
                String apiData = DbConnection.executeQuery(conn, EXT_API_SQL, bizType, dbApiCode);
                if (apiData.isEmpty()) {
                    logger.error("数据库中没有对应的接口规范数据");
                } else {
                    FileUtils.writeFile(apiData, infileParentPath
                        + bizType + File.separator + dbApiCode + ".data");
                }
            }
        }
    }
    /**
     * 导出指定业务种类、接口规范id的接口规范数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param params2 参数2
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam2(Connection conn, String params1, String params2, String infileParentPath){
        String[] bizTypeArr = params1.split(SPLIT_REGEX);
        for (String bizType : bizTypeArr) {
            String[] apiCodeArr = params2.split(SPLIT_REGEX);
            for (String apiCode : apiCodeArr) {
                String apiData = DbConnection.executeQuery(conn, EXT_API_SQL, bizType, apiCode);
                if (apiData.isEmpty()) {
                    logger.error("数据库中没有对应的接口规范数据");
                } else {
                    FileUtils.writeFile(apiCode, infileParentPath
                        + bizType + File.separator + apiCode + ".data");
                }
            }
        }
    }

}
