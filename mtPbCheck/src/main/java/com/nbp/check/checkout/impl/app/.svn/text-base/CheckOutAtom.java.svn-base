package com.nbp.check.checkout.impl.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nbp.db.sql.AtomSqlConstants.ATOMCODES_SQL;
import static com.nbp.db.sql.AtomSqlConstants.ATOM_SQL;
import static com.nbp.db.sql.AtomSqlConstants.ATOM_SQL_WITH_PROID;
import static com.nbp.db.sql.AtomSqlConstants.STREAMCODES_SQL;
import static com.nbp.db.sql.AtomSqlConstants.STREAMCODES_SQL_WITH_ATOMTRANCODE;
import static com.nbp.db.sql.AtomSqlConstants.STREAMCODES_SQL_WITH_PROID;
import static com.nbp.db.sql.AtomSqlConstants.STREAM_SQL;
import static com.nbp.db.sql.AtomSqlConstants.STREAM_SQL_WITH_PROID;
import static com.nbp.util.Constants.ALL_REGEX;
import static com.nbp.util.Constants.ATOM_DIR;
import static com.nbp.util.Constants.SPLIT_REGEX;

/**
 * 导出原子交易数据类
 */
public class CheckOutAtom extends CheckOutExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CheckOutAtom.class);

    @Override
    public void execute(Connection conn, String proIds, String params1, String params2, String filePath) {
        logger.info("开始导出原子交易数据");
        Long startTime = Timer.getStartTime();
        String infileParentPath = filePath + File.separator + ATOM_DIR + File.separator;
        if (ALL_REGEX.equals(proIds) && ALL_REGEX.equals(params1)){
            executorCheckOutAll(conn, infileParentPath);
        } else if (ALL_REGEX.equals(params1)){
            executorCheckOutByProId(conn, proIds, infileParentPath);
        } else {
            executorCheckOutByParam1(conn, params1, infileParentPath);
        }
        logger.info("导出原子交易数据结束,用时:{}ms", Timer.getUsedTime(startTime));
    }

    /**
     * 导出所有原子交易数据
     * @param conn 数据库连接
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutAll(Connection conn, String infileParentPath){
        String streamData = DbConnection.executeQuery(conn, STREAM_SQL);
        if ("".equals(streamData)){
            logger.error("数据库中没有对应的原子交易目录数据");
        } else {
            FileUtils.writeFile(streamData, infileParentPath + "atomdir.data");
            List<String> streamCodes = DbConnection.excuteQueryList(conn, STREAMCODES_SQL);
            for (String streamCode : streamCodes) {
                List<String> atomCodes = DbConnection.excuteQueryList(conn, ATOMCODES_SQL, streamCode);
                for (String atomCode : atomCodes) {
                    String data = DbConnection.executeQuery(conn, ATOM_SQL, streamCode, atomCode);
                    if ("".equals(data)) {
                        logger.error("数据库中没有streamCode={},atomCode={}的原子交易数据", streamCode, atomCode);
                    } else {
                        FileUtils.writeFile(data, infileParentPath + streamCode
                                + File.separator + atomCode + ".data");
                    }
                }
            }
        }
    }

    /**
     * 导出指定工程id的原子交易数据
     * @param conn 数据库连接
     * @Param proIds 工程id
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByProId(Connection conn, String proIds, String infileParentPath){
        String streamData = DbConnection.executeQuery(conn, STREAM_SQL_WITH_PROID, proIds);
        if ("".equals(streamData)){
            logger.error("数据库中没有proId={}的原子交易目录数据", proIds);
        } else {
            FileUtils.writeFile(streamData, infileParentPath + "atomdir.data");
            List<String> streamCodes = DbConnection.excuteQueryList(conn, STREAMCODES_SQL_WITH_PROID,proIds);
            for (String streamCode  : streamCodes) {
                List<String> atomCodes = DbConnection.excuteQueryList(conn, ATOMCODES_SQL, streamCode);
                if (atomCodes.isEmpty()) {
                    logger.error("数据库中没有streamCode={}的原子交易数据", streamCode);
                } else {
                    for (String atomCode : atomCodes) {
                        String data = DbConnection.executeQuery(conn, ATOM_SQL_WITH_PROID, streamCode, atomCode, proIds);
                        if (!"".equals(data)) {
                            FileUtils.writeFile(data, infileParentPath + streamCode
                                    + File.separator + atomCode + ".data");
                        }
                    }
                }
            }
        }
    }

    /**
     * 导出指定原子交易码的原子交易数据
     * @param conn 数据库连接
     * @param params1 参数1
     * @param infileParentPath 导出文件所在目录
     */
    private void executorCheckOutByParam1(Connection conn, String params1, String infileParentPath){
        String[] atomTranCodeArr = params1.split(SPLIT_REGEX);
        Set<String> streamCodeSet = new HashSet<>();
        for (String atomTranCode : atomTranCodeArr) {
            List<String> streamCodes = DbConnection.excuteQueryList(conn, STREAMCODES_SQL_WITH_ATOMTRANCODE, atomTranCode);
            if (streamCodes.isEmpty()){
                logger.error("数据库中没有atomTranCode={}的原子交易", atomTranCode);
            } else {
                for (String streamCode : streamCodes) {
                    streamCodeSet.add(streamCode);
                    String data = DbConnection.executeQuery(conn, ATOM_SQL, streamCode, atomTranCode);
                    if ("".equals(data)) {
                        logger.error("数据库中没有数据库中没有streamCode={},atomTranCode={}的原子交易", streamCode, atomTranCode);
                    } else {
                        FileUtils.writeFile(data, infileParentPath
                                + streamCode + File.separator + atomTranCode + ".data");
                    }
                }
            }
        }
        // 导出原子交易目录数据
        if (!streamCodeSet.isEmpty()){
            StringBuilder streamSql = new StringBuilder(STREAM_SQL);
            streamSql.append(" where STREAMCODE in (");
            for (int i = 0; i < streamCodeSet.size(); i++) {
                streamSql.append("?");
                if (i != streamCodeSet.size() - 1){
                    streamSql.append(",");
                } else {
                    streamSql.append(")");
                }
            }
            String streamData = DbConnection.executeQuery(conn, streamSql.toString(),streamCodeSet.toArray());
            if (!"".equals(streamData)) {
                FileUtils.writeFile(streamData, infileParentPath + "atomdir.data");
            }
        }
    }

}
