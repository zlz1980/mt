package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.SysCfgSqlConstants.SYS_CFG_SQL_WITH_PROID;
import static com.nbp.db.sql.SysCfgSqlConstants.SYS_CFG_SQL;
import static com.nbp.db.sql.SysCfgSqlConstants.SYS_CFG_SQL_WITH_DEFNAME;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.SPLIT_REGEX;
import static com.nbp.util.Constants.SYS_DIR;

/**
 * 导出应用系统参数类
 */
public class CheckOutSysCfg extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutSysCfg.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出应用系统参数");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + SYS_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出应用系统参数结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有应用系统参数数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, SYS_CFG_SQL);
        if ("".equals(data)){
            logger.error("数据库中没有对应的应用系统参数数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "syscfg.data");
        }
    }
    /**
     * 导出指定工程id下的系统参数数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, SYS_CFG_SQL_WITH_PROID, proIds);
        if ("".equals(data)){
            logger.error("数据库中没有proId={}的参数数据", proIds);
        } else {
            FileUtils.writeFile(data, infileParentPath + "syscfg.data");
        }
    }
    /**
     * 导出指定defName的应用系统参数数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String [] defNameArr = params1.split(SPLIT_REGEX);
        for(String defName : defNameArr){
            String data = DbConnection.executeQuery(conn, SYS_CFG_SQL_WITH_DEFNAME,defName);
            if ("".equals(data)){
                logger.error("数据库中没有defName = "+ defName + "的应用系统参数数据");
            } else {
                FileUtils.writeFile(data, infileParentPath + defName + ".data");
            }
        }
    }
}
