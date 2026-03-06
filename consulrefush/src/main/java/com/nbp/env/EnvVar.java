package com.nbp.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnvVar {
    private static final Logger logger = LoggerFactory.getLogger(EnvVar.class);
    public static String envService = null;
    public static String envAz = null;
    public static String ALL_AZ = "ALL";
    public static String aclToken = null;
    public static String ip = null;
    public static String port = null;

    public static Map<String, String> envmap = new HashMap<>();
    /**
     * 初始化环境变量配置信息
     *
     * <p>从系统环境变量中加载服务名称、标志位、ACL令牌、IP地址和端口号等配置信息，
     * 当标志位为"ALL_AZ"时进行特殊处理，并将所有配置信息存入Map集合返回。</p>
     *
     * @param service 环境变量中服务名称对应的key
     * @param flag 环境变量中标志位对应的key（特殊值"ALL_AZ"将直接作为标志位值）
     * @return 包含所有环境变量配置的Map集合，包含以下key：
     *         <ul>
     *           <li>service参数对应的服务名称</li>
     *           <li>flag参数对应的标志位值</li>
     *           <li>"ACLTOKEN"对应的访问令牌</li>
     *           <li>"IP"对应的IP地址</li>
     *           <li>"PORT"对应的端口号</li>
     *         </ul>
     */
    public static Map<String,String> init(String service , String flag) {
        // 初始化环境变量，从环境变量中获取服务名称
        envService = System.getenv(service);
        // 初始化环境变量，从环境变量中获取标志位,如果标志位为ALL_AZ则特殊处理
        if (ALL_AZ.equals(flag)) {
            envAz = ALL_AZ;
        }else {
            envAz = System.getenv(flag);
        }
        // 初始化环境变量，从环境变量中获取ACLTOKEN、IP和PORT
        aclToken = System.getenv("ACLTOKEN");
        ip = System.getenv("CONSULIP");
        port = System.getenv("CONSULPORT");
        logger.info("配置信息加载完成，当前配置如下：");
        logger.info("SERVICE={}", envService);
        logger.info("FLAG={}", envAz);
        envmap.put(flag, envAz);
        envmap.put(service, envService);
        envmap.put("ACLTOKEN",aclToken);
        envmap.put("IP",ip);
        envmap.put("PORT",port);
        return envmap;
    }
}
