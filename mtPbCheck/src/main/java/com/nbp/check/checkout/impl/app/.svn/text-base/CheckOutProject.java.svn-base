package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_DIR_SQL;
import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_DIR_SQL_WITH_PROID;
import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_INFO_SQL;
import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_INFO_SQL_WITH_PROID;
import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_SQL;
import static com.nbp.db.sql.ProjectSqlConstants.PROJECT_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.PROJECT_DIR;

/**
 * 导出工程信息类
 */
public class CheckOutProject extends CheckOutExecutor {
    private final Logger logger = LoggerFactory.getLogger(CheckOutProject.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出工程信息");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + PROJECT_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if(!ALL_REGEX.equals(proIds)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else if(!ALL_REGEX.equals(params1)) {
            executorCheckOutByProId(conn, params1, infileParentPath);
        }
        logger.info("导出工程信息结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有工程信息数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String data = DbConnection.executeQuery(conn, PROJECT_SQL)
                + DbConnection.executeQuery(conn, PROJECT_DIR_SQL)
                + DbConnection.executeQuery(conn, PROJECT_INFO_SQL);
        if (data.isEmpty()) {
            logger.error("数据库中没有对应的工程信息");
        } else {
            FileUtils.writeFile(data, infileParentPath + "project.data");
        }
    }
    /**
     * 导出指定工程id的工程信息数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String data = DbConnection.executeQuery(conn, PROJECT_SQL_WITH_PROID, proIds)
                + DbConnection.executeQuery(conn, PROJECT_DIR_SQL_WITH_PROID, proIds)
                + DbConnection.executeQuery(conn, PROJECT_INFO_SQL_WITH_PROID, proIds);
        if (data.isEmpty()) {
            logger.error("数据库中没有proId = " + proIds + "的工程信息");
        } else {
            FileUtils.writeFile(data, infileParentPath + proIds + ".data");
        }
    }

}
