package com.nbp.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.nbp.util.Constants.CHECK_IN;
import static com.nbp.util.Constants.CHECK_OUT;

public class EnvVar {
    private static final Logger logger = LoggerFactory.getLogger(EnvVar.class);
    public static String envHome = null;
    public static String envURL = null;
    public static String envUSER = null;
    public static String envPASSWORD = null;
    public static String envDbDriver = null;
    public static String outBaseDir = null;

    /**
     * 读取配置文件，并初始化导出配置信息
     */
    public static void initCheckOut() {
        envHome = System.getenv("CHECKHOME");
        envURL = System.getenv("PBCHKOUT_URL");
        envUSER = System.getenv("PBCHKOUT_USER");
        envPASSWORD = System.getenv("PBCHKOUT_PASSWORD");
        envDbDriver = System.getenv("DBDRIVER");
        outBaseDir = envHome + File.separator + "data";
        logger.info("配置信息加载完成，当前配置如下：");
        logger.info("CHECKHOME={}", envHome);
        logger.info("PBCHKOUT_URL={}", envURL);
        logger.info("PBCHKOUT_USER={}", envUSER);
        logger.info("PBCHKOUT_PASSWORD={}", envPASSWORD);
        logger.info("DBDRIVER={}", envDbDriver);
    }

    /**
     * 读取配置文件，并初始化配置信息
     */
    public static void initCheckIn() {
        envHome = System.getenv("CHECKHOME");
        envURL = System.getenv("PBCHKIN_URL");
        envUSER = System.getenv("PBCHKIN_USER");
        envPASSWORD = System.getenv("PBCHKIN_PASSWORD");
        envDbDriver = System.getenv("DBDRIVER");
        outBaseDir = envHome + File.separator + "data";
        logger.info("配置信息加载完成，当前配置如下：");
        logger.info("CHECKHOME={}", envHome);
        logger.info("PBCHKIN_URL={}", envURL);
        logger.info("PBCHKIN_USER={}", envUSER);
        logger.info("PBCHKIN_PASSWORD={}", envPASSWORD);
        logger.info("DBDRIVER={}", envDbDriver);
    }
}
