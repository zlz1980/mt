package com.nbp.check.checkout;

import com.nbp.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.nbp.util.Constants.*;

/**
 * 导出执行父类
 */
public abstract class CheckOutExecutor {
    private final Logger logger = LoggerFactory.getLogger(CheckOutExecutor.class);

    /**
     * 执行导出任务
     * @param proIds 工程ids
     * @Param params1 参数1
     * @Param params2 参数2
     * @Param filePath 导出目录
     */
    public abstract void execute(Connection conn, String proIds, String params1, String params2, String filePath);

    /**
     * 清理输出目录
     * @param filePath 待清理目录
     */
    public void clearOutputDirectory(String filePath) {
        File outputDir = new File(filePath);
        if (outputDir.exists()) {
            logger.info("目录已存在{}自动清理并继续", filePath);
            FileUtils.deleteDir(outputDir);
        }
    }

    /**
     * 创建数据标识文件
     * 用于按工程导出时标识数据类型方便后续更新更新对应版本号
     * APP.data应用数据标识文件 GW.data网关数据标识文件
     * @param filePath 文件路径
     */
    public void writeFileData (String filePath) {
        String [] appDirs = {SYS_DIR,BATCH_DIR,ATOM_DIR,RULE_DIR,SQL_DIR,
            STATIC_DIR,ERRORCODE_DIR,ERRORTYPE_DIR,HTTPCONF_DIR,TRAN_DIR,CHNL_DIR,
            PROJECT_DIR,UTIL_DIR,MODULE_DIR,EXTAPI_DIR};
        String [] gwDirs = {GW_STATIC_DIR,GW_SYS_DIR,GW_RULE_DIR,
            GW_TESTROUTE_DIR,GW_TRANROUTE_DIR,GW_EXCEPTION_DIR};
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            Set<String> appDirSet = new HashSet<>(Arrays.asList(appDirs));
            Set<String> gwDirSet = new HashSet<>(Arrays.asList(gwDirs));
            if (Arrays.stream(file.list()).anyMatch(appDirSet::contains)){
                File appFile = new File(filePath + File.separator + "APP.data");
                try {
                    appFile.createNewFile();
                } catch (Exception e) {
                    logger.error("创建APP.data文件失败", e);
                    throw new RuntimeException(e);
                }
            }
            if (Arrays.stream(file.list()).anyMatch(gwDirSet::contains)){
                File gwFile = new File(filePath + File.separator + "GW.data");
                try {
                    gwFile.createNewFile();
                } catch (Exception e) {
                    logger.error("创建GW.data文件失败", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
