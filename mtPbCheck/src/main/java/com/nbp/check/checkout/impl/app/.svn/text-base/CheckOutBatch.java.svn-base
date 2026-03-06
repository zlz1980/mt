package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.BatchSqlConstants.BATCH_SQL;
import static com.nbp.db.sql.BatchSqlConstants.BATCH_SQL_WITH_PROID;
import static com.nbp.db.sql.BatchSqlConstants.BATCH_SQL_WITH_TASKNAME;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.BATCH_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出批量处理数据类
 */
public class CheckOutBatch extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutBatch.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始批量任务数据导出");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + BATCH_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("批量任务数据导出结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有批量任务数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, BATCH_SQL);
        if ("".equals(data)){
            logger.error("数据库中没有对应的批量任务数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "batch.data");
        }
    }

    /**
     * 导出批量任务数据(根据工程ID)
     * @param conn 数据库连接
     * @Param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, BATCH_SQL_WITH_PROID, proIds);
        if ("".equals(data)){
            logger.error("数据库中没有proId = {}批量任务数据", proIds);
        } else {
            FileUtils.writeFile(data, infileParentPath + "batch.data");
        }
    }

    /**
     * 导出指定任务名称的批量任务数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String [] batchNameArr = params1.split(SPLIT_REGEX);
        for(String batchName : batchNameArr){
            String data = DbConnection.executeQuery(conn, BATCH_SQL_WITH_TASKNAME,batchName);
            if ("".equals(data)){
                logger.error("数据库中没有batchName ={}的批量任务数据", batchName);
            } else {
                FileUtils.writeFile(data, infileParentPath + batchName + ".data");
            }
        }
    }
}
