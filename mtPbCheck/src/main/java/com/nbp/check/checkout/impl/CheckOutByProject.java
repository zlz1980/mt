package com.nbp.check.checkout.impl;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.check.checkout.impl.app.CheckOutAtom;
import com.nbp.check.checkout.impl.app.CheckOutBatch;
import com.nbp.check.checkout.impl.app.CheckOutChnl;
import com.nbp.check.checkout.impl.app.CheckOutErrorCode;
import com.nbp.check.checkout.impl.app.CheckOutErrorType;
import com.nbp.check.checkout.impl.app.CheckOutExtApi;
import com.nbp.check.checkout.impl.app.CheckOutHttpConf;
import com.nbp.check.checkout.impl.app.CheckOutModule;
import com.nbp.check.checkout.impl.app.CheckOutProject;
import com.nbp.check.checkout.impl.app.CheckOutRuleGroup;
import com.nbp.check.checkout.impl.app.CheckOutSqlTemplate;
import com.nbp.check.checkout.impl.app.CheckOutStatictbl;
import com.nbp.check.checkout.impl.app.CheckOutSysCfg;
import com.nbp.check.checkout.impl.app.CheckOutTran;
import com.nbp.check.checkout.impl.app.CheckOutUtilBean;
import com.nbp.check.checkout.impl.gw.CheckOutGwException;
import com.nbp.check.checkout.impl.gw.CheckOutGwRuleGroup;
import com.nbp.check.checkout.impl.gw.CheckOutGwStatictbl;
import com.nbp.check.checkout.impl.gw.CheckOutGwSysCfg;
import com.nbp.check.checkout.impl.gw.CheckOutGwTestRoute;
import com.nbp.check.checkout.impl.gw.CheckOutGwTranRoute;
import com.nbp.util.CheckingUtils;
import com.nbp.db.DbConnection;
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
 * 按工程导出的执行器类
 */
public class CheckOutByProject extends CheckOutExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutByProject.class);
    /**
     * 按工程导出的执行器列表
     */
    private final List<CheckOutExecutor> checkOutList = new ArrayList<CheckOutExecutor>(21){
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
            add(new CheckOutExtApi());
            add(new CheckOutGwStatictbl());
            add(new CheckOutGwRuleGroup());
            add(new CheckOutGwSysCfg());
            add(new CheckOutGwTestRoute());
            add(new CheckOutGwTranRoute());
            add(new CheckOutGwException());
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
            // 如果是单个工程ID，则直接导出到指定目录
            if (proIdArr.length == 1) {
                executeCheckOutStep(conn ,proIdArr[0], params1, params2, filePath);
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
     * 执行按工程导出的执行器
     */
    private void executeCheckOutStep (Connection conn, String proId, String params1, String params2, String filePath){
        CheckingUtils.clearTableNum();
        this.checkOutList.forEach(checkOutExecutor -> checkOutExecutor.execute(conn, proId, params1, params2, filePath));
        CheckingUtils.checking(conn, proId);
        writeFileData(filePath);
    }

}
