package com.nbp.app;

import com.nbp.util.ConsulInfoFetcherUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 虚拟机内存刷新
 */
public class ConsulInfoFetcher {
    private static final String[] SERVICE = {"GW","APP","BAT"};
    private static final String SPLIT_FLAG =":";
    private static final String[] CONTAIN ={"ALL","AZ1","AZ2","AZ3"};
    private static final Logger logger = LoggerFactory.getLogger(ConsulInfoFetcher.class);

    private static final String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    // 验证端口号的正则表达式 (0-65535)
    private static final String PORT_REGEX =
        "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    /**
     * 主函数 - 用于执行Consul服务内存刷新操作
     *
     * <p>支持两种刷新方式：
     * 1. 根据服务类型(GW/APP/BAT)和机房标识(ALL/AZ1/AZ2/AZ3)刷新
     * 2. 根据指定IP和端口直接刷新
     *
     * <p>执行流程：
     * 1. 验证参数数量必须为2个
     * 2. 根据参数类型选择不同的刷新策略
     * 3. 处理刷新结果并输出错误信息
     *
     * @param args 命令行参数数组，包含两种形式：
     *             - 形式1: [服务类型, 机房标识] 如 ["GW", "ALL"]
     *             - 形式2: [IP地址, 端口号] 如 ["10.0.0.1", "8765"]
     *
     * <p>错误处理：
     * - 参数数量不正确时立即退出并提示用法
     * - 参数格式不正确时立即退出并提示用法
     * - 刷新失败时记录错误日志并返回非零退出码
     */
    public static void main(String[] args) {
        //自动化部署内存刷新
        if (args.length != 2) {
            logger.error("参数缺失，请输入正确的参数：{}", Arrays.toString(args));
            System.out.println("参数错误"+Arrays.toString(args)+"请输入正确的参数，例：\n" +
                "GW ALL\n" +
                "GW AZ1\n" +
                "GW AZ2\n" +
                "GW AZ3\n" +
                "BAT ALL\n" +
                "BAT AZ1\n" +
                "BAT AZ2\n" +
                "BAT AZ3\n" +
                "APP ALL\n" +
                "APP AZ1\n" +
                "APP AZ2\n" +
                "APP AZ3\n" +
                "或正确的IP和端口" );
            System.exit(-1);
        }else{
            ConsulInfoFetcherUtils consulInfoFetcherUtils = new ConsulInfoFetcherUtils();
            Map<String,String> mapError = new HashMap<>();
            List<Map<String,String>> listError = new ArrayList<>();
            if ((Arrays.asList(SERVICE).contains(args[0])) && Arrays.asList(CONTAIN).contains(args[1])) {
                List<String> list = consulInfoFetcherUtils.consulInfo(args[0], args[1]);
                if (list.size() == 0){
                    logger.error("未查询到对应ip和端口：{}", Arrays.toString(args));
                    System.out.println("未查询到对应ip和端口："+Arrays.toString(args)+"请输入正确的参数：");
                    System.exit(-1);
                }else {
                    for (String consulIp : list) {
                        String ip = consulIp.split(SPLIT_FLAG)[0];
                        String port = consulIp.split(SPLIT_FLAG)[1];
                        try {
                            mapError = consulInfoFetcherUtils.refushServices(ip, port);
                            if(!mapError.isEmpty()){
                                listError.add(mapError);
                            }
                        } catch (Exception e) {
                            logger.error("刷新失败！", e);
                        }
                    }
                }
            } else if ((args[0].matches(ipPattern)) && (args[1].matches(PORT_REGEX))) {
                try {
                    mapError = consulInfoFetcherUtils.refushServices(args[0], args[1]);
                    if(!mapError.isEmpty()){
                        listError.add(mapError);
                    }
                }catch (Exception e){
                    logger.error("刷新失败！",e);
                }
            }else {
                logger.error("参数错误，请输入正确的参数：{}", Arrays.toString(args));
                System.out.println("参数错误"+Arrays.toString(args)+"请输入正确的参数，例：\n" +
                    "GW ALL\n" +
                    "GW AZ1\n" +
                    "GW AZ2\n" +
                    "GW AZ3\n" +
                    "BAT ALL\n" +
                    "BAT AZ1\n" +
                    "BAT AZ2\n" +
                    "BAT AZ3\n" +
                    "APP ALL\n" +
                    "APP AZ1\n" +
                    "APP AZ2\n" +
                    "APP AZ3\n" +
                    "或正确的IP和端口" );
                System.exit(-1);
            }
            if (!listError.isEmpty()){
                logger.info("参数{}，刷新失败！",Arrays.toString(args));
                System.out.println("参数"+Arrays.toString(args)+"，刷新失败！" );
                System.exit(-1);
            }else {
                logger.info("参数{}，刷新成功！",Arrays.toString(args));
                System.out.println("参数"+Arrays.toString(args)+"，刷新成功！" );
                System.exit(0);
            }
        }
    }
}
