package com.nbp.app;

import com.nbp.bak.BakUtils;
import com.nbp.check.checkin.CheckIn;
import com.nbp.db.DbConnection;
import com.nbp.env.EnvVar;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;

import static com.nbp.env.EnvVar.envDbDriver;
import static com.nbp.env.EnvVar.envHome;
import static com.nbp.env.EnvVar.envPASSWORD;
import static com.nbp.env.EnvVar.envURL;
import static com.nbp.env.EnvVar.envUSER;
import static com.nbp.env.EnvVar.outBaseDir;
import static com.nbp.util.Constants.BAK;
import static com.nbp.util.Constants.PROJECT_INFO;
import static com.nbp.util.Tips.CHECK_IN_EXPLAIN;

/**
 * 导入程序入口类
 */
public class PbCheckIn {

    private static final Logger logger = LoggerFactory.getLogger(PbCheckIn.class);

    /**
     * 导入程序入口
     * @param args 导入参数
     */
    public static void main(String[] args) {
        EnvVar.initCheckIn();
        try (Connection conn = DbConnection.getConn()){
            if (envHome == null || envURL == null || envUSER == null || envPASSWORD == null || envDbDriver == null) {
                logger.error("环境变量配置不完整，请检查 CHECKHOME, PBCHKIN_URL, PBCHKIN_USER, PBCHKIN_PASSWORD, DBDRIVER 是否已正确设置。");
                System.exit(1);
            }
            if (args.length == 1 && BAK.equals(args[0])) {
                try {
                    BakUtils.bakAll(conn);
                } catch (Exception e) {
                    logger.error("备份失败", e);
                    System.exit(1);
                }
            } else if (args.length == 1){
                logger.info("导入文件开始");
                FileUtils.readMenuFile(outBaseDir + File.separator + args[0] + File.separator + PROJECT_INFO);
                CheckIn checkIn = new CheckIn();
                Long startTime = Timer.getStartTime();
                checkIn.execute(conn, outBaseDir + File.separator + args[0]);
                logger.info("导入文件结束，总耗时: {}ms", Timer.getUsedTime(startTime));
            } else {
                logger.error("参数错误，请输入正确的参数：");
                logger.error(CHECK_IN_EXPLAIN);
                System.exit(1);
            }
        } catch (Exception e) {
            logger.error("导入失败", e);
            System.exit(1);
        }
    }

}
