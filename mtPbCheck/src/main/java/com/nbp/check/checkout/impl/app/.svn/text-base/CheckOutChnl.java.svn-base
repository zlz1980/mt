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

import static com.nbp.db.sql.ChnlSqlConstants.CHNLNOS_SQL;
import static com.nbp.db.sql.ChnlSqlConstants.CHNLNOS_SQL_WITH_PROID;
import static com.nbp.db.sql.ChnlSqlConstants.CHNL_SQL;
import static com.nbp.db.sql.ChnlSqlConstants.CHNL_SQL_WITH_PROID;
import static com.nbp.db.sql.ChnlSqlConstants.FTRANCODES_SQL;
import static com.nbp.db.sql.ChnlSqlConstants.TRAN_CODE_CONV_SQL_WITH_CHNLAN_AND_FTRANCODE;
import static com.nbp.db.sql.ChnlSqlConstants.TRAN_CODE_CONV_SQL_WITH_PROID;
import static com.nbp.db.sql.ChnlSqlConstants.TRAN_CODE_SQL_WITH_CHNLAN_AND_FTRANCODE;
import static com.nbp.db.sql.ChnlSqlConstants.TRAN_PKG_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.CHNL_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出外部交易数据类
 */
public class CheckOutChnl extends CheckOutExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CheckOutChnl.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出外部交易码数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + CHNL_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else if (ALL_REGEX.equals(params2)) {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        } else {
            executorCheckOutByParam2(conn, params1, params2, infileParentPath);
        }
        logger.info("导出外部交易码数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有外部交易码数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String chnlData = DbConnection.executeQuery(conn, CHNL_SQL);
        if ("".equals(chnlData)) {
            logger.error("数据库中没有对应的渠道数据");
        } else {
            FileUtils.writeFile(chnlData, infileParentPath + "chnl.data");
            List<String> dbChnlNos = DbConnection.excuteQueryList(conn, CHNLNOS_SQL);
            for (String dbChnlNo : dbChnlNos) {
                List<String> dbFTranCodes = DbConnection.excuteQueryList(conn, FTRANCODES_SQL, dbChnlNo);
                if (dbFTranCodes.isEmpty()) {
                    logger.error("数据库中没有chnlNo={}对应的外部交易码数据", dbChnlNo);
                } else {
                    for (String dbFTranCode : dbFTranCodes) {
                        String data = DbConnection.executeQuery(conn, TRAN_CODE_SQL_WITH_CHNLAN_AND_FTRANCODE, dbChnlNo, dbFTranCode)
                                + DbConnection.executeQuery(conn, TRAN_CODE_CONV_SQL_WITH_CHNLAN_AND_FTRANCODE, dbChnlNo, dbFTranCode);
                        if (data.isEmpty()) {
                            logger.error("数据库中没有chnlNo={},fTranCode={}对应的外部交易码数据", dbChnlNo, dbFTranCode);
                        } else {
                            FileUtils.writeFile(data, infileParentPath
                                    + dbChnlNo + File.separator + dbFTranCode + ".data");
                        }
                    }
                }
            }
        }
    }

    /**
     * 导出指定工程id的外部交易码数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String chnlData = DbConnection.executeQuery(conn, CHNL_SQL_WITH_PROID, proIds);
        if ("".equals(chnlData)) {
            logger.error("数据库中没有proId={}对应的渠道数据", proIds);
        } else {
            FileUtils.writeFile(chnlData, infileParentPath + "chnl.data");
            List<String> dbChnlNos = DbConnection.excuteQueryList(conn, CHNLNOS_SQL_WITH_PROID, proIds);
            for (String dbChnlNo : dbChnlNos) {
                List<String> dbFTranCodes = DbConnection.excuteQueryList(conn, FTRANCODES_SQL, dbChnlNo);
                if (dbFTranCodes.isEmpty()) {
                    logger.error("数据库中没有chnlNo={}对应的外部交易码数据", dbChnlNo);
                } else {
                    for (String dbFTranCode : dbFTranCodes) {
                        String data = DbConnection.executeQuery(conn, TRAN_PKG_SQL_WITH_PROID, dbChnlNo, dbFTranCode, proIds)
                                + DbConnection.executeQuery(conn, TRAN_CODE_CONV_SQL_WITH_PROID, dbChnlNo, dbFTranCode, proIds);
                        if (!data.isEmpty()) {
                            FileUtils.writeFile(data, infileParentPath
                                    + dbChnlNo + File.separator + dbFTranCode + ".data");
                        }
                    }
                }
            }
        }
    }

    /**
     * 导出指定渠道下的外部交易码数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] chnlNoArr = params1.split(SPLIT_REGEX);
        StringBuilder chnlSql = new StringBuilder(CHNL_SQL);
        chnlSql.append(" where CHNLNO in (");
        for (int i = 0; i < chnlNoArr.length; i++) {
            List<String> dbFTranCodes = DbConnection.excuteQueryList(conn, FTRANCODES_SQL, chnlNoArr[i]);
            if (dbFTranCodes.isEmpty()) {
                logger.error("数据库中没有chnlNo={}对应的外部交易码数据", chnlNoArr[i]);
            } else {
                for (String dbFTranCode : dbFTranCodes) {
                    String data = DbConnection.executeQuery(conn, TRAN_CODE_SQL_WITH_CHNLAN_AND_FTRANCODE, chnlNoArr[i], dbFTranCode)
                            + DbConnection.executeQuery(conn, TRAN_CODE_CONV_SQL_WITH_CHNLAN_AND_FTRANCODE, chnlNoArr[i], dbFTranCode);
                    if (!data.isEmpty()) {
                        FileUtils.writeFile(data, infileParentPath
                                + chnlNoArr[i] + File.separator + dbFTranCode + ".data");
                    }
                }
            }
            chnlSql.append("?");
            if (i != chnlNoArr.length - 1) {
                chnlSql.append(",");
            } else {
                chnlSql.append(")");
            }
        }
        String chnlData = DbConnection.executeQuery(conn, chnlSql.toString(), (Object[]) chnlNoArr);
        if (!"".equals(chnlData)) {
            FileUtils.writeFile(chnlData, infileParentPath + "chnl.data");
        }
    }

    /**
     * 导出指定渠道与外部交易码的外部交易码数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param params2 参数2
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam2(Connection conn, String params1, String params2, String infileParentPath) {
        String[] chnlNoArr = params1.split(SPLIT_REGEX);
        StringBuilder chnlSql = new StringBuilder(CHNL_SQL);
        chnlSql.append(" where CHNLNO in (");
        for (int i = 0; i < chnlNoArr.length; i++) {
            String[] fTranCodeArr = params2.split(SPLIT_REGEX);
            for (String fTranCode : fTranCodeArr) {
                String data = DbConnection.executeQuery(conn, TRAN_CODE_SQL_WITH_CHNLAN_AND_FTRANCODE, chnlNoArr[i], fTranCode)
                        + DbConnection.executeQuery(conn, TRAN_CODE_CONV_SQL_WITH_CHNLAN_AND_FTRANCODE, chnlNoArr[i], fTranCode);
                if (data.isEmpty()) {
                    logger.error("数据库中没有chnlNo={},fTranCode={}对应的外部交易码数据", chnlNoArr[i], fTranCode);
                } else {
                    FileUtils.writeFile(data, infileParentPath
                            + chnlNoArr[i] + File.separator + fTranCode + ".data");
                }
            }
            chnlSql.append("?");
            if (i != chnlNoArr.length - 1) {
                chnlSql.append(",");
            } else {
                chnlSql.append(")");
            }
        }
        String chnlData = DbConnection.executeQuery(conn, chnlSql.toString(), (Object[]) chnlNoArr);
        if (!"".equals(chnlData)) {
            FileUtils.writeFile(chnlData, infileParentPath + "chnl.data");
        }
    }
}
