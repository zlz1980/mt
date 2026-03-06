package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.ErrorTypeSqlConstants.ERRORTYPE_SQL;
import static com.nbp.db.sql.ErrorTypeSqlConstants.ERRORTYPE_SQL_WITH_ID;
import static com.nbp.db.sql.ErrorTypeSqlConstants.ERRORTYPE_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.ERRORTYPE_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出错误码类型数据类
 */
public class CheckOutErrorType extends CheckOutExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CheckOutErrorType.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出错误码类型数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + ERRORTYPE_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出错误码类型数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有错误码类型数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, ERRORTYPE_SQL);
        if ("".equals(data)) {
            logger.error("数据库中没有对应的错误码类型数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "errortype.data");
        }
    }
    /**
     * 导出指定工程id的错误码类型数据
     * @param conn 数据库连接
     * @Param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, ERRORTYPE_SQL_WITH_PROID, proIds);
        if ("".equals(data)) {
            logger.error("数据库中没有proId = " + proIds + "的错误码类型数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "errortype.data");
        }
    }
    /**
     * 导出指定错误码的错误码数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] typeArr = params1.split(SPLIT_REGEX);
        for (String type : typeArr) {
            String data = DbConnection.executeQuery(conn, ERRORTYPE_SQL_WITH_ID, type);
            if ("".equals(data)) {
                logger.error("数据库中没有errortype = {}的错误码类型数据", type);
            } else {
                FileUtils.writeFile(data, infileParentPath + type + ".data");
            }
        }
    }
}
