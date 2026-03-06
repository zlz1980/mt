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

import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_FTRANCODES_SQL;
import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_FTRANCODES_SQL_WITH_PROID;
import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_REQCHNLS_SQL;
import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_REQCHNLS_SQL_WITH_PROID;
import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_SQL_WITH_PROID;
import static com.nbp.db.sql.GwTestRouteSqlConstants.GW_TESTROUTE_SQL_WITH_REQCHNL_AND_FTRANCODE;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.GW_TESTROUTE_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出网关灰度路由控制数据类
 */
public class CheckOutGwTestRoute extends CheckOutExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CheckOutGwTestRoute.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出灰度路由控制数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + GW_TESTROUTE_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)){
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1) && ALL_REGEX.equals(params2)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else if (ALL_REGEX.equals(params2)) {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        } else {
            executorCheckOutByParam2(conn, params1, params2, infileParentPath);
        }
        logger.info("导出灰度路由控制数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }
    /**
     * 导出所有网关灰度路由控制数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        List<String> reqChnls = DbConnection.excuteQueryList(conn, GW_TESTROUTE_REQCHNLS_SQL);
        for (String reqChnl : reqChnls) {
            List<String> fTranCodes = DbConnection.excuteQueryList(conn, GW_TESTROUTE_FTRANCODES_SQL, reqChnl);
            for (String fTranCode : fTranCodes) {
                String data = DbConnection.executeQuery(conn, GW_TESTROUTE_SQL_WITH_REQCHNL_AND_FTRANCODE, reqChnl, fTranCode);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + fTranCode + ".data");
                }
            }
        }
    }
    /**
     * 导出指定工程id的网关灰度路由控制数据
     * @param conn 数据库连接
     * @param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        List<String> reqChnls = DbConnection.excuteQueryList(conn, GW_TESTROUTE_REQCHNLS_SQL_WITH_PROID, proIds);
        for (String reqChnl : reqChnls) {
            List<String> fTranCodes = DbConnection.excuteQueryList(conn, GW_TESTROUTE_FTRANCODES_SQL_WITH_PROID, reqChnl, proIds);
            for (String fTranCode : fTranCodes) {
                String data = DbConnection.executeQuery(conn, GW_TESTROUTE_SQL_WITH_PROID, reqChnl, fTranCode, proIds);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + fTranCode + ".data");
                }
            }
        }
    }
    /**
     * 导出指定reqChnl的网关灰度路由控制数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String [] reqChnls = params1.split(SPLIT_REGEX);
        for (String reqChnl : reqChnls) {
            List<String> fTranCodes = DbConnection.excuteQueryList(conn, GW_TESTROUTE_FTRANCODES_SQL, reqChnl);
            if (fTranCodes.isEmpty()) {
                logger.error("reqChnl={}不存在灰度路由控制数据", reqChnl);
            } else {
                for (String fTranCode : fTranCodes) {
                    String data = DbConnection.executeQuery(conn, GW_TESTROUTE_SQL_WITH_REQCHNL_AND_FTRANCODE, reqChnl, fTranCode);
                    if (!"".equals(data)) {
                        FileUtils.writeFile(data, infileParentPath
                                + reqChnl + File.separator + fTranCode + ".data");
                    }
                }
            }
        }
    }
    /**
     * 导出指定reqChnl、fTranCode的网关灰度路由控制数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param params2 参数2
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam2(Connection conn, String params1, String params2, String infileParentPath){
        String [] reqChnls = params1.split(SPLIT_REGEX);
        String [] fTranCodes = params2.split(SPLIT_REGEX);
        for (String reqChnl : reqChnls) {
            for (String fTranCode : fTranCodes) {
                String data = DbConnection.executeQuery(conn, GW_TESTROUTE_SQL_WITH_REQCHNL_AND_FTRANCODE, reqChnl, fTranCode);
                if (!"".equals(data)) {
                    FileUtils.writeFile(data, infileParentPath
                            + reqChnl + File.separator + fTranCode + ".data");
                } else {
                    logger.error("reqChnl={}, fTranCode={}不存在灰度路由控制数据", reqChnl, fTranCode);
                }
            }
        }
    }

}
