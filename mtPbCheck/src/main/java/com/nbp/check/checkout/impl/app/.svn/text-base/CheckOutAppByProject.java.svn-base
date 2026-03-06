package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.CheckingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.nbp.db.sql.ProjectSqlConstants.PROIDS_SQL;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 按工程导出应用相关表类
 */
public class CheckOutAppByProject extends CheckOutExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutAppByProject.class);
    /**
     * 按工程导出应用相关表执行器列表
     */
    private final List<CheckOutExecutor> CHECK_OUT_LIST = new ArrayList<CheckOutExecutor>(14){
        {
            add(new CheckOutBatch());
            add(new CheckOutSysCfg());
            add(new CheckOutAtom());
            add(new CheckOutRuleGroup());
            add(new CheckOutSqlTemplate());
            add(new CheckOutStatictbl());
            add(new CheckOutErrorCode());
            add(new CheckOutErrorType());
            add(new CheckOutHttpConf());
            add(new CheckOutTran());
            add(new CheckOutChnl());
            add(new CheckOutProject());
            add(new CheckOutUtilBean());
            add(new CheckOutModule());
        }
    };

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        List<String> dbProIds = DbConnection.excuteQueryList(conn, PROIDS_SQL);
        if (!dbProIds.isEmpty() && ALL_REGEX.equals(proIds)) {
            for (String dbProId : dbProIds) {
                String proIdPath = filePath + File.separator + dbProId;
                executeCheckOutStep(conn, dbProId, params1, params2, proIdPath);
            }
        } else if (!dbProIds.isEmpty()) {
            String [] proIdArr = proIds.split(SPLIT_REGEX);
            if (proIdArr.length == 1) {
                executeCheckOutStep(conn, proIdArr[0], params1, params2, filePath);
            } else {
                for (String proId : proIdArr) {
                    if (dbProIds.contains(proId)) {
                        String proIdPath = filePath + File.separator + proId;
                        executeCheckOutStep(conn, proId, params1, params2, proIdPath);
                    } else {
                        logger.error("工程ID={}不存在数据", proId);
                    }
                }
            }
        } else {
            logger.error("没有查询到工程ID");
        }
    }

    /**
     * 执行按工程导出应用相关表执行器
     */
    private void executeCheckOutStep (Connection conn, String proId, String params1, String params2, String filePath){
        // 工程导出开始时清除表数据量统计
        CheckingUtils.clearTableNum();
        this.CHECK_OUT_LIST.forEach(checkOutExecutor -> checkOutExecutor.execute(conn, proId, params1, params2, filePath));
        // 工程导出结束时校验表数量统计
        CheckingUtils.checking(conn, proId);
        writeFileData(filePath);
    }

}
