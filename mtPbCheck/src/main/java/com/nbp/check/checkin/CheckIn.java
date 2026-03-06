package com.nbp.check.checkin;

import com.nbp.db.DbConnection;
import com.nbp.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nbp.util.Constants.ATOM_DIR;
import static com.nbp.util.Constants.BATCH_DIR;
import static com.nbp.util.Constants.CHNL_DIR;
import static com.nbp.util.Constants.DATA_NUM;
import static com.nbp.util.Constants.DELETE_SQL_LIST_NAME;
import static com.nbp.util.Constants.ERRORCODE_DIR;
import static com.nbp.util.Constants.ERRORTYPE_DIR;
import static com.nbp.util.Constants.EXTAPI_DIR;
import static com.nbp.util.Constants.GW_EXCEPTION_DIR;
import static com.nbp.util.Constants.GW_RULE_DIR;
import static com.nbp.util.Constants.GW_STATIC_DIR;
import static com.nbp.util.Constants.GW_SYS_DIR;
import static com.nbp.util.Constants.GW_TESTROUTE_DIR;
import static com.nbp.util.Constants.GW_TRANROUTE_DIR;
import static com.nbp.util.Constants.HTTPCONF_DIR;
import static com.nbp.util.Constants.IMPORT_COMMIT_SIZE;
import static com.nbp.util.Constants.INSERT_SQL_LIST_NAME;
import static com.nbp.util.Constants.MODULE_DIR;
import static com.nbp.util.Constants.PROJECT_DIR;
import static com.nbp.util.Constants.RULE_DIR;
import static com.nbp.util.Constants.SQL_DIR;
import static com.nbp.util.Constants.STATIC_DIR;
import static com.nbp.util.Constants.SYS_DIR;
import static com.nbp.util.Constants.TRAN_DIR;
import static com.nbp.util.Constants.UTIL_DIR;

/**
 * @author w46838
 * 导入主程序类
 */
public class CheckIn {

    private final Logger logger = LoggerFactory.getLogger(CheckIn.class);

    /**
     * 执行导入
     * @param filepath 导入文件目录
     */
    public void execute(Connection conn, String filepath) {
        File file = new File(filepath);
        if(!file.exists() || !file.isDirectory()){
            logger.error("导入文件目录{}不存在",filepath);
            return;
        }
        //定义模块导入顺序
        String [] appDirs = {SYS_DIR,BATCH_DIR,ATOM_DIR,RULE_DIR,SQL_DIR,
                STATIC_DIR,ERRORCODE_DIR,ERRORTYPE_DIR,HTTPCONF_DIR,TRAN_DIR,CHNL_DIR,
                PROJECT_DIR,UTIL_DIR,MODULE_DIR,EXTAPI_DIR};
        String [] gwDirs = {GW_STATIC_DIR,GW_SYS_DIR,GW_RULE_DIR,
                GW_TESTROUTE_DIR,GW_TRANROUTE_DIR,GW_EXCEPTION_DIR};
        List<String> fileList = new ArrayList<>();
        for (String subDir : appDirs) {
            File appFile = new File(filepath, subDir);
            if (appFile.exists()) {
                fileList.addAll(FileUtils.getFileList(appFile));
            }
        }
        for (String subDir : gwDirs) {
            File gwFile = new File(filepath, subDir);
            if (gwFile.exists()) {
                fileList.addAll(FileUtils.getFileList(gwFile));
            }
        }
        for(String filePath : FileUtils.getDifference(fileList)){
            importFile(conn,new File(filePath));
        }
    }

    /**
     * 导入文件逻辑
     * @param file 导入目录中的模块
     */
    public void importFile(Connection conn, File file) {
        logger.info("开始执行导入文件：{}",file.getAbsolutePath());
        Map<String, Object> sqlMap = FileUtils.readFile(file);
        @SuppressWarnings("unchecked")
        List<String> deleteSqlList = (List<String>) sqlMap.get(DELETE_SQL_LIST_NAME);
        @SuppressWarnings("unchecked")
        List<String> insertSqlList = (List<String>) sqlMap.get(INSERT_SQL_LIST_NAME);
        int dataNum = sqlMap.get(DATA_NUM) == null ? 0 : Integer.parseInt(sqlMap.get(DATA_NUM).toString());
        int successNum = 0;
        //delete语句与insert语句总数<=5000时放在同一个事务中执行
        if (deleteSqlList.size() + insertSqlList.size() <= IMPORT_COMMIT_SIZE) {
            List<String> executeSqlList = new ArrayList<>();
            executeSqlList.addAll(deleteSqlList);
            executeSqlList.addAll(insertSqlList);
            successNum = DbConnection.executeUpdate(conn, executeSqlList);
        } else {
            for (int i = 0; i < deleteSqlList.size(); i += IMPORT_COMMIT_SIZE) {
                List<String> subDeleteSqlList = deleteSqlList.subList(i, Math.min(i + IMPORT_COMMIT_SIZE, deleteSqlList.size()));
                DbConnection.executeUpdate(conn, subDeleteSqlList);
            }
            for (int i = 0; i < insertSqlList.size(); i += IMPORT_COMMIT_SIZE) {
                List<String> subInsertSqlList = insertSqlList.subList(i, Math.min(i + IMPORT_COMMIT_SIZE, insertSqlList.size()));
                successNum += DbConnection.executeUpdate(conn, subInsertSqlList);
            }
        }
        if (dataNum == successNum) {
            logger.info("预计导入数据条数为{}，与实际导入一致",dataNum);
        } else {
            logger.error("导入文件：{}",file.getAbsolutePath());
            logger.error("预计导入数据条数为{}，与实际导入条数{}不一致",dataNum,successNum);
            throw new RuntimeException("导入文件异常");
        }
    }

}
