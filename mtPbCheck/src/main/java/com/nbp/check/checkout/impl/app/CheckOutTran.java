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

import static com.nbp.db.sql.ModuleSqlConstants.MODULE_SQL_WITH_TRANCODE;
import static com.nbp.db.sql.TranSqlConstants.BUSITYPES_SQL;
import static com.nbp.db.sql.TranSqlConstants.BUSITYPES_SQL_WITH_PROID;
import static com.nbp.db.sql.TranSqlConstants.BUSI_TYPE_SQL;
import static com.nbp.db.sql.TranSqlConstants.BUSI_TYPE_SQL_WITH_PROID;
import static com.nbp.db.sql.TranSqlConstants.FLOW_PARA_SQL;
import static com.nbp.db.sql.TranSqlConstants.FLOW_PARA_SQL_WITH_PROID;
import static com.nbp.db.sql.TranSqlConstants.FLOW_SQL;
import static com.nbp.db.sql.TranSqlConstants.FLOW_SQL_WITH_PROID;
import static com.nbp.db.sql.TranSqlConstants.TRANCODES_SQL;
import static com.nbp.db.sql.TranSqlConstants.TRANCODES_SQL_WITH_PROID;
import static com.nbp.db.sql.TranSqlConstants.TRAN_CODE_SQL;
import static com.nbp.db.sql.TranSqlConstants.TRAN_CODE_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.MODULE_BUSITYPE;
import static com.nbp.util.Constants.MODULE_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;
import static com.nbp.util.Constants.TRAN_DIR;

/**
 * 导出内部交易数据类
 */
public class CheckOutTran extends CheckOutExecutor {

    private final Logger logger = LoggerFactory.getLogger(CheckOutTran.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始执行导出内部交易码数据");
        Long startTime = Timer.getStartTime();
        //内部交易、交易组件数据导出目录
        String infileParentPath = filePath + File.separator + TRAN_DIR + File.separator;
        //交易组件与组件目录对应关系导出目录
        String modulePath = filePath + File.separator + MODULE_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutAll(conn, infileParentPath, modulePath);
        } else if (ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)) {
            executorCheckOutByProId(conn, proIds, infileParentPath, modulePath);
        } else if (ALL_REGEX.equals(params2)) {
            executorCheckOutByParam1(conn, params1, infileParentPath, modulePath);
        } else {
            executorCheckOutByParam2(conn, params1, params2, infileParentPath, modulePath);
        }
        logger.info("导出内部交易码数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有内部交易数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     * @Param modulePath 模块与模块目录对应关系导出目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath, String modulePath){
        String data = DbConnection.executeQuery(conn, BUSI_TYPE_SQL);
        if ("".equals(data)) {
            logger.error("数据库中没有对应的业务种类数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "busitype.data");
            List<String> dbBusiTypes = DbConnection.excuteQueryList(conn, BUSITYPES_SQL);
            for (String dbBusiType : dbBusiTypes) {
                List<String> dbTranCodes = DbConnection.excuteQueryList(conn, TRANCODES_SQL, dbBusiType);
                for (String dbTranCode : dbTranCodes) {
                    String tranData = DbConnection.executeQuery(conn, TRAN_CODE_SQL, dbBusiType, dbTranCode)
                            + DbConnection.executeQuery(conn, FLOW_SQL, dbBusiType, dbTranCode)
                            + DbConnection.executeQuery(conn, FLOW_PARA_SQL, dbBusiType, dbTranCode);
                    if (tranData.isEmpty()) {
                        logger.error("数据库中没有对应的内部交易码数据");
                    } else {
                        FileUtils.writeFile(tranData, infileParentPath
                                + dbBusiType + File.separator + dbTranCode + ".data");
                        //导出的业务种类为业务模块时，需同步导出模块与模块目录关联关系
                        if (MODULE_BUSITYPE.equals(dbBusiType)){
                            executorCheckOutModule(conn, dbTranCode, modulePath);
                        }
                    }
                }
            }
        }
    }
    /**
     * 导出指定工程id的内部交易数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath, String modulePath){
        String data = DbConnection.executeQuery(conn, BUSI_TYPE_SQL_WITH_PROID, proIds);
        if ("".equals(data)) {
            logger.error("数据库中没有proId = " + proIds + "的业务种类数据");
        } else {
            FileUtils.writeFile(data, infileParentPath + "busitype.data");
            List<String> dbBusiTypes = DbConnection.excuteQueryList(conn, BUSITYPES_SQL_WITH_PROID, proIds);
            for (String dbBusiType : dbBusiTypes) {
                List<String> dbTranCodes = DbConnection.excuteQueryList(conn, TRANCODES_SQL_WITH_PROID, dbBusiType, proIds);
                for (String dbTranCode : dbTranCodes) {
                    String tranData = DbConnection.executeQuery(conn, TRAN_CODE_SQL_WITH_PROID, dbBusiType, dbTranCode, proIds)
                            + DbConnection.executeQuery(conn, FLOW_SQL_WITH_PROID, dbBusiType, dbTranCode, proIds)
                            + DbConnection.executeQuery(conn, FLOW_PARA_SQL_WITH_PROID, dbBusiType, dbTranCode, proIds);
                    if (tranData.isEmpty()) {
                        logger.error("数据库中没有对应的内部交易码数据");
                    } else {
                        FileUtils.writeFile(tranData, infileParentPath
                                + dbBusiType + File.separator + dbTranCode + ".data");
                        //导出的业务种类为业务模块时，需同步导出模块与模块目录关联关系
                        if (MODULE_BUSITYPE.equals(dbBusiType)){
                            executorCheckOutModule(conn, dbTranCode, modulePath);
                        }
                    }
                }
            }
        }
    }
    /**
     * 导出指定业务种类下的内部交易数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 内部交易、交易组件导出文件所在目录
     * @param modulePath 组件与组件目录关联关系导出文件
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath, String modulePath){
        String[] bizTypeArr = params1.split(SPLIT_REGEX);
        StringBuilder busiTypeSql = new StringBuilder(BUSI_TYPE_SQL);
        busiTypeSql.append(" where BUSITYPE in (");
        for (int i = 0; i < bizTypeArr.length; i++) {
            List<String> dbTranCodes = DbConnection.excuteQueryList(conn, TRANCODES_SQL, bizTypeArr[i]);
            for (String dbTranCode : dbTranCodes) {
                String tranData = DbConnection.executeQuery(conn, TRAN_CODE_SQL, bizTypeArr[i], dbTranCode)
                        + DbConnection.executeQuery(conn, FLOW_SQL, bizTypeArr[i], dbTranCode)
                        + DbConnection.executeQuery(conn, FLOW_PARA_SQL, bizTypeArr[i], dbTranCode);
                if (tranData.isEmpty()) {
                    logger.error("数据库中没有对应的内部交易码数据");
                } else {
                    FileUtils.writeFile(tranData, infileParentPath
                            + bizTypeArr[i] + File.separator + dbTranCode + ".data");
                    //导出的业务种类为业务模块时，需同步导出模块与模块目录关联关系
                    if (MODULE_BUSITYPE.equals(bizTypeArr[i])){
                        executorCheckOutModule(conn, dbTranCode, modulePath);
                    }
                }
            }
            busiTypeSql.append("?");
            if (i != bizTypeArr.length - 1) {
                busiTypeSql.append(",");
            } else {
                busiTypeSql.append(")");
            }
        }
        String busiData = DbConnection.executeQuery(conn, busiTypeSql.toString(), (Object[]) bizTypeArr);
        if (!"".equals(busiData)) {
            FileUtils.writeFile(busiData, infileParentPath + "busitype.data");
        }
    }
    /**
     * 导出指定业务种类、内部交易码的内部交易数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param params2 参数2
     * @param infileParentPath 内部交易、交易组件导出文件所在目录
     * @param modulePath 组件与组件目录关联关系导出文件
     */
    private void executorCheckOutByParam2(Connection conn, String params1, String params2, String infileParentPath, String modulePath){
        String[] bizTypeArr = params1.split(SPLIT_REGEX);
        StringBuilder busiTypeSql = new StringBuilder(BUSI_TYPE_SQL);
        busiTypeSql.append(" where BUSITYPE in (");
        for (int i = 0; i < bizTypeArr.length; i++) {
            String[] tranCodeArr = params2.split(SPLIT_REGEX);
            for (String tranCode : tranCodeArr) {
                String tranData = DbConnection.executeQuery(conn, TRAN_CODE_SQL, bizTypeArr[i], tranCode)
                        + DbConnection.executeQuery(conn, FLOW_SQL, bizTypeArr[i], tranCode)
                        + DbConnection.executeQuery(conn, FLOW_PARA_SQL, bizTypeArr[i], tranCode);
                if (tranData.isEmpty()) {
                    logger.error("数据库中没有对应的内部交易码数据");
                } else {
                    FileUtils.writeFile(tranData, infileParentPath
                            + bizTypeArr[i] + File.separator + tranCode + ".data");
                    //导出的业务种类为业务模块时，需同步导出模块与模块目录关联关系
                    if (MODULE_BUSITYPE.equals(bizTypeArr[i])){
                        executorCheckOutModule(conn, tranCode, modulePath);
                    }
                }
            }
            busiTypeSql.append("?");
            if (i != bizTypeArr.length - 1) {
                busiTypeSql.append(",");
            } else {
                busiTypeSql.append(")");
            }
        }
        String busiData = DbConnection.executeQuery(conn, busiTypeSql.toString(), (Object[]) bizTypeArr);
        if (!"".equals(busiData)) {
            FileUtils.writeFile(busiData, infileParentPath + "busitype.data");
        }
    }

    /**
     * 导出组件与组件目录的关系
     * @param conn 数据库连接
     * @param tranCode 组件交易码
     * @param modulePath 导出文件所在目录
     */
    private void executorCheckOutModule(Connection conn, String tranCode, String modulePath){
        String data = DbConnection.executeQuery(conn, MODULE_SQL_WITH_TRANCODE, tranCode);
        if ("".equals(data)){
            logger.error("数据库中没有tranCode={}的组件与组件目录关系数据", tranCode);
        } else {
            FileUtils.writeFile(data, modulePath + tranCode + ".data");
        }
    }

}
