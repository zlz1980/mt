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

import static com.nbp.db.sql.RuleSqlConstants.BIZTYPES_SQL;
import static com.nbp.db.sql.RuleSqlConstants.BIZTYPES_SQL_WITH_PROID;
import static com.nbp.db.sql.RuleSqlConstants.RULE_GROUP_SQL_WITH_BIZTYPE;
import static com.nbp.db.sql.RuleSqlConstants.RULE_GROUP_SQL_WITH_PROID;
import static com.nbp.db.sql.RuleSqlConstants.RULE_SET_SQL_WITH_BIZTYPE;
import static com.nbp.db.sql.RuleSqlConstants.RULE_SET_SQL_WITH_PROID;
import static com.nbp.db.sql.RuleSqlConstants.RULE_SQL_WITH_BIZTYPE;
import static com.nbp.db.sql.RuleSqlConstants.RULE_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.RULE_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出应用规则组数据类
 */
public class CheckOutRuleGroup extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutRuleGroup.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出应用规则组数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + RULE_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)){
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("应用规则组数据导出结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有应用规则组数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        List<String> bizTypes = DbConnection.excuteQueryList(conn, BIZTYPES_SQL);
        for(String bizType:bizTypes){
            String data = DbConnection.executeQuery(conn, RULE_GROUP_SQL_WITH_BIZTYPE, bizType)
                    + DbConnection.executeQuery(conn, RULE_SET_SQL_WITH_BIZTYPE, bizType)
                    + DbConnection.executeQuery(conn, RULE_SQL_WITH_BIZTYPE, bizType);
            if (!data.isEmpty()) {
                FileUtils.writeFile(data, infileParentPath + bizType +".data");
            }
        }
    }

    /**
     * 导出指定工程id的应用规则组数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        List<String> bizTypes = DbConnection.excuteQueryList(conn, BIZTYPES_SQL_WITH_PROID, proIds, proIds);
        for(String bizType:bizTypes) {
            String data = DbConnection.executeQuery(conn, RULE_GROUP_SQL_WITH_PROID, bizType, proIds)
                    + DbConnection.executeQuery(conn, RULE_SET_SQL_WITH_PROID, bizType, proIds)
                    + DbConnection.executeQuery(conn, RULE_SQL_WITH_PROID, bizType, proIds);
            if (data.isEmpty()) {
                logger.error("数据库中没有proIds = {},bizType = {}的应用规则组数据", proIds, bizType);
            } else {
                FileUtils.writeFile(data, infileParentPath + bizType +".data");
            }
        }
    }
    /**
     * 导出指定规则类型的应用规则组数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String [] bizTypeArr = params1.split(SPLIT_REGEX);
        for(String bizType:bizTypeArr){
            String data = DbConnection.executeQuery(conn, RULE_GROUP_SQL_WITH_BIZTYPE, bizType)
                    + DbConnection.executeQuery(conn, RULE_SET_SQL_WITH_BIZTYPE, bizType)
                    + DbConnection.executeQuery(conn, RULE_SQL_WITH_BIZTYPE, bizType);
            if (data.isEmpty()){
                logger.error("数据库中没有bizType = "+ bizType + "的应用规则组数据");
            } else {
                FileUtils.writeFile(data, infileParentPath + bizType + ".data");
            }
        }
    }

}
