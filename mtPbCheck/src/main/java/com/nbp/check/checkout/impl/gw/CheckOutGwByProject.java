package com.nbp.check.checkout.impl.gw;

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
 * 按工程导出网关相关表类
 */
public class CheckOutGwByProject extends CheckOutExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutGwByProject.class);
    /**
     * 按工程导出网关相关表执行器列表
     */
    private final List<CheckOutExecutor> checkOutList = new ArrayList<CheckOutExecutor>(6){
        {
            add(new CheckOutGwStatictbl());
            add(new CheckOutGwRuleGroup());
            add(new CheckOutGwSysCfg());
            add(new CheckOutGwTestRoute());
            add(new CheckOutGwTranRoute());
            add(new CheckOutGwException());
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
            // 如果是单个工程ID，则直接导出到指定目录
            if (proIdArr.length == 1) {
                executeCheckOutStep(conn, proIdArr[0], params1, params2, filePath);
            } else {
                for (String proId : proIdArr) {
                    if (dbProIds.contains(proId)) {
                        executeCheckOutStep(conn, proId, params1, params2, filePath);
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
     * 执行按工程导出网关相关表执行器
     */
    private void executeCheckOutStep (Connection conn, String proId, String param1, String param2, String filePath){
        CheckingUtils.clearTableNum();
        this.checkOutList.forEach(CheckOutExecutor -> execute(conn, proId, param1, param2, filePath));
        CheckingUtils.checking(conn, proId);
        writeFileData(filePath);
    }

}
