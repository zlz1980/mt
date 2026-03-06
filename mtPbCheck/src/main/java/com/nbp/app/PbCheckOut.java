package com.nbp.app;

import com.nbp.check.checkout.CheckOutExecutor;
import com.nbp.check.checkout.CheckOutFactory;
import com.nbp.check.checkout.ExecutorParams;
import com.nbp.db.DbConnection;
import com.nbp.env.EnvVar;
import com.nbp.util.FileUtils;
import com.nbp.util.Timer;
import com.sun.javafx.binding.StringFormatter;
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
import static com.nbp.util.Constants.PROJECT_INFO;
import static com.nbp.util.Tips.CHECK_OUT_EXPLAIN;

/**
 * 导出程序入口类
 */
public class PbCheckOut {

    private static final Logger logger = LoggerFactory.getLogger(PbCheckOut.class);

    /**
     * 导出程序入口
     * @param args 导出参数
     */
    public static void main(String[]args) {
        EnvVar.initCheckOut();
        try (Connection conn = DbConnection.getConn()){
            if (envHome == null || envURL == null || envUSER == null || envPASSWORD == null || envDbDriver == null) {
                logger.error("环境变量配置不完整，请检查 CHECKHOME, PBCHKOUT_URL, PBCHKOUT_USER, PBCHKOUT_PASSWORD, DBDRIVER 是否已正确设置。");
                System.exit(1);
            }
            CheckOutExecutor checkOut = CheckOutFactory.createCheckout(args);
            if (checkOut != null) {
                logger.info("开始导出文件...");
                String filePath = outBaseDir + File.separator + args[args.length - 1];
                checkOut.clearOutputDirectory(filePath);
                Long startTime = Timer.getStartTime();
                ExecutorParams params = argsToParams(args);
                checkOut.execute(conn, params.getProIds(), params.getParam1(), params.getParam2(), params.getFilePath());
                FileUtils.writeMenuFile(filePath + File.separator + PROJECT_INFO);
                logger.info("导出文件结束，总耗时: {}ms", Timer.getUsedTime(startTime));
            } else {
                logger.error("参数错误，请输入正确的参数：");
                logger.error(CHECK_OUT_EXPLAIN);
                System.exit(1);
            }
        } catch (Exception e) {
            logger.error("导出失败", e);
            System.exit(1);
        }
    }

    /**
     * 输入参数转换执行器使用参数
     * @param args 输入参数
     * @return 执行器使用参数
     */
    private static ExecutorParams argsToParams(String [] args) {
        ExecutorParams params = new ExecutorParams();
        params.setProIds("%");
        params.setParam1("%");
        params.setParam2("%");
        params.setFilePath(outBaseDir + File.separator + args[args.length - 1]);
        switch (args.length) {
            // 入参为3个参数，获取工程导出执行器
            case 3:
                params.setProIds(args[1]);
                break;
            // 入参为4个参数，获取单项导出执行器
            case 4:
                params.setParam1(args[2]);
                break;
            // 入参为5个参数，获取单项导出执行器
            case 5:
                params.setParam1(args[2]);
                params.setParam2(args[3]);
                break;
        }
        return params;
    }

}
