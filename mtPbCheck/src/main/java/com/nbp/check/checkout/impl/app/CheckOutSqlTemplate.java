package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.SqlTemplateSqlConstants.TEMPLATE_SQL;
import static com.nbp.db.sql.SqlTemplateSqlConstants.TEMPLATE_SQL_WITH_PROID;
import static com.nbp.db.sql.SqlTemplateSqlConstants.TEMPLATE_SQL_WITH_SQLNAME;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.SPLIT_REGEX;
import static com.nbp.util.Constants.SQL_DIR;

/**
 * 导出sql配置数据类
 */
public class CheckOutSqlTemplate extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutSqlTemplate.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出SQL配置");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + SQL_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出SQL配置结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有sql配置数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, TEMPLATE_SQL);
        if ("".equals(data)) {
            logger.error("数据库中没有对应的SQL配置数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "sqltemplate.data");
        }
    }

    /**
     * 导出指定工程id的sql配置数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, TEMPLATE_SQL_WITH_PROID, proIds);
        if ("".equals(data)) {
            logger.error("数据库中没有proId = " + proIds + "的SQL配置数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "sqltemplate.data");
        }
    }
    /**
     * 导出指定sql名称的sql配置数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] sqlNameArr = params1.split(SPLIT_REGEX);
        for (String sqlName : sqlNameArr) {
            String data = DbConnection.executeQuery(conn, TEMPLATE_SQL_WITH_SQLNAME, sqlName);
            if ("".equals(data)) {
                logger.error("数据库中没有sqlName = " + sqlName + "的SQL配置数据");
            } else {
                FileUtils.writeFile(data, infileParentPath + sqlName + ".data");
            }
        }
    }
}
