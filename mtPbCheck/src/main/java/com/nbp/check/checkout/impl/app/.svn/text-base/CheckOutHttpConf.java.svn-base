package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.HttpConfSqlConstants.HTTP_SQL;
import static com.nbp.db.sql.HttpConfSqlConstants.HTTP_SQL_WITH_PROID;
import static com.nbp.db.sql.HttpConfSqlConstants.HTTP_SQL_WITH_SERNAME;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.HTTPCONF_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出http通讯数据类
 */
public class CheckOutHttpConf extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutHttpConf.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出HTTP通讯数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + HTTPCONF_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出HTTP通讯数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有http通讯数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, HTTP_SQL);
        if ("".equals(data)) {
            logger.error("数据库中没有对应的HTTP通讯数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "httpconf.data");
        }
    }
    /**
     * 导出指定工程id的http通讯数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, HTTP_SQL_WITH_PROID, proIds);
        if ("".equals(data)) {
            logger.error("数据库中没有proId = " + proIds + "的HTTP通讯数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "httpconf.data");
        }
    }
    /**
     * 导出指定服务名的http通讯数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] serNameArr = params1.split(SPLIT_REGEX);
        for (String serName : serNameArr) {
            String data = DbConnection.executeQuery(conn, HTTP_SQL_WITH_SERNAME, serName);
            if ("".equals(data)) {
                logger.error("数据库中没有serName = " + serName + "的HTTP通讯数据");
            } else {
                FileUtils.writeFile(data, infileParentPath + serName + ".data");
            }
        }
    }

}
