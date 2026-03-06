package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.ModuleSqlConstants.MODULE_DIR_SQL;
import static com.nbp.db.sql.ModuleSqlConstants.MODULE_DIR_SQL_WITH_PROID;
import static com.nbp.db.sql.ModuleSqlConstants.MODULE_DIR_SQL_WITH_TYPE;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.MODULE_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

public class CheckOutModule extends CheckOutExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CheckOutModule.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出组件目录数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + MODULE_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)){
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出组件目录数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有组件目录数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, MODULE_DIR_SQL);
        if ("".equals(data)) {
            logger.error("数据库中没有对应的组件目录数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "module.data");
        }
    }

    /**
     * 导出指定工程id的组件目录数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, MODULE_DIR_SQL_WITH_PROID, proIds);
        if ("".equals(data)){
            logger.error("数据库中没有proId={}的组件目录数据", proIds);
        } else {
            FileUtils.writeFile(data, infileParentPath + "module.data");
        }
    }

    /**
     * 导出指定组件类型的组件目录数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] typeArr = params1.split(SPLIT_REGEX);
        for (String type : typeArr){
            String data = DbConnection.executeQuery(conn, MODULE_DIR_SQL_WITH_TYPE, type);
            if ("".equals(data)){
                logger.error("数据库中没有type={}的组件目录数据", type);
            } else {
                FileUtils.writeFile(data, infileParentPath + type + ".data");
            }
        }
    }

}
