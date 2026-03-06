package com.nbp.check.checkout.impl.gw;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_CODES_SQL;
import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_CODES_SQL_WITH_PROID;
import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_REQCHNLS_SQL;
import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_REQCHNLS_SQL_WITH_PROID;
import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_SQL_WITH_PROID;
import static com.nbp.db.sql.GwExceptionSqlConstants.GW_EXCEPTION_SQL_WITH_REQCHNL_AND_CODE;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.GW_EXCEPTION_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

public class CheckOutGwException extends CheckOutExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CheckOutGwException.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出网关异常默认报文返回数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + GW_EXCEPTION_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)){
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else if (ALL_REGEX.equals(params2)) {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        } else {
            executorCheckOutByParam2(conn, params1, params2, infileParentPath);
        }
        logger.info("导出网关异常默认报文数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有网关异常默认报文数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        List<String> reqChnls = DbConnection.excuteQueryList(conn, GW_EXCEPTION_REQCHNLS_SQL);
        for (String reqChnl : reqChnls) {
            List<String> codes = DbConnection.excuteQueryList(conn, GW_EXCEPTION_CODES_SQL, reqChnl);
            for (String code : codes) {
                String data = DbConnection.executeQuery(conn, GW_EXCEPTION_SQL_WITH_REQCHNL_AND_CODE, reqChnl, code);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + code + ".data");
                }
            }
        }
    }
    /**
     * 导出指定工程id的网关异常默认报文数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        List<String> reqChnls = DbConnection.excuteQueryList(conn, GW_EXCEPTION_REQCHNLS_SQL_WITH_PROID, proIds);
        for (String reqChnl : reqChnls) {
            List<String> codes = DbConnection.excuteQueryList(conn, GW_EXCEPTION_CODES_SQL_WITH_PROID, reqChnl, proIds);
            for (String code : codes) {
                String data = DbConnection.executeQuery(conn, GW_EXCEPTION_SQL_WITH_PROID, reqChnl, code, proIds);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + code + ".data");
                }
            }
        }
    }
    /**
     * 导出指定reqChnl的网关异常默认报文数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String [] reqChnls = params1.split(SPLIT_REGEX);
        for (String reqChnl : reqChnls) {
            List<String> codes = DbConnection.excuteQueryList(conn, GW_EXCEPTION_CODES_SQL, reqChnl);
            if (codes.isEmpty()) {
                logger.error("reqChnl={}不存在网关异常默认报文返回数据", reqChnl);
            } else {
                for (String code : codes) {
                    String data = DbConnection.executeQuery(conn, GW_EXCEPTION_SQL_WITH_REQCHNL_AND_CODE, reqChnl, code);
                    if (!"".equals(data)) {
                        FileUtils.writeFile(data, infileParentPath
                                + reqChnl + File.separator + code + ".data");
                    }
                }
            }
        }
    }
    /**
     * 导出指定reqChnl、code的网关异常默认报文数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param params2 参数2
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam2(Connection conn, String params1, String params2, String infileParentPath){
        String [] reqChnls = params1.split(SPLIT_REGEX);
        String [] codes = params2.split(SPLIT_REGEX);
        for (String reqChnl : reqChnls) {
            for (String code : codes) {
                String data = DbConnection.executeQuery(conn, GW_EXCEPTION_SQL_WITH_REQCHNL_AND_CODE, reqChnl, code);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + code + ".data");
                } else {
                    logger.error("reqChnl={}, fTranCode={}不存在网关异常默认报文返回数据", reqChnl, code);
                }
            }
        }
    }

}
