package com.nbp.util;

import com.alibaba.fastjson.JSON;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.CatalogServicesRequest;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.nbp.env.EnvVar;
import com.nbp.vo.ConsulInfoFetcherVo;
import com.nbp.vo.RefreshResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsulInfoFetcherUtils {

    private static final String STATEMENT = "com.nbp.util.ConsulInfoFetcherUtils.";
    //consul的token
    private static final String ACLTOKEN = "ACLTOKEN";
    private static final String WEBURL = "http://";
    private static final String ADDRESSPATH = "/initAllCache?token=";
    private static final String ALL = "ALL";
    private static final String SPLIT_FLAG = ":";
    private static final Logger logger = LoggerFactory.getLogger(ConsulInfoFetcherUtils.class);
    private static final String[] CONSUL_SERVICE_NAME = {"EBAC-SUBNOA-MS-Acceptance",
        "EBAC-SUBNOA-MS-BatchExecuteService",
        "EBAC-SUBNOA-MS-CFinService",
        "EBAC-SUBNOA-MS-CNonFinService",
        "EBAC-SUBNOA-MS-CupsGateway",
        "EBAC-SUBNOA-MS-DFinService",
        "EBAC-SUBNOA-MS-DNonFinService",
        "EBAC-SUBNOA-MS-EmvoGateway",
        "EBAC-SUBNOA-MS-Gateway",
        "EBAC-SUBNOA-MS-ManageService",
        "EBAC-SUBNOA-MS-Verification"};
    /**
     * 根据服务类型和区域标志查询Consul注册中心的服务节点信息
     *
     * <p>该方法通过Consul客户端API查询指定服务在指定数据中心(可用区)的健康节点信息，
     * 返回需要刷新的IP地址和端口列表。</p>
     *
     * @param service1 服务类型参数，可选值：GW(网关)、APP(应用)、BAT(批处理)或直接指定IP地址
     * @param flag 区域标志参数，可选值：AZ1(可用区1)、AZ2(可用区2)、AZ3(可用区3)或ALL(全部可用区)
     * @return 包含需要刷新的服务节点IP和端口列表，格式为"IP:PORT"
     *
     * @throws Exception 当Consul查询失败时抛出异常并终止程序
     *
     * <p>处理流程：
     * 1. 初始化环境变量并创建Consul客户端
     * 2. 根据区域标志获取目标数据中心列表
     * 3. 查询每个数据中心下的服务列表
     * 4. 过滤出符合条件的健康服务节点
     * 5. 组装需要刷新的节点信息</p>
     */
    /**
     * 根据条件查询consul信息
     *
     * @param service1 第一个参数（GW,APP,BAT或ip）
     * @param flag 第二个参数(AZ1,AZ2,AZ3,ALL)
     * @return 需要刷新的ip和端口
     */
    public List<String> consulInfo(String service1, String flag) {
        logger.info("方法{},ConsulInfo START" , STATEMENT);
        logger.info("request params:{}{}" , service1 , flag);
        Map<String, String> servicesMap = EnvVar.init(service1, flag);
        ConsulClient consClient = new ConsulClient(servicesMap.get("IP"), Integer.parseInt(servicesMap.get("PORT")));
        String az = servicesMap.get(flag);
        String aclToken = servicesMap.get(ACLTOKEN);
        String consulServices = servicesMap.get(service1);
        String[] consulService = consulServices.split(",");
        List<String> consulServeList = new ArrayList<>(Arrays.asList(consulService));
        List<String> consulList = new ArrayList<>();
        List<ConsulInfoFetcherVo> listNode = new ArrayList<>();
        Response<List<String>> response = consClient.getCatalogDatacenters();
        Map<String, List<String>> serviceNameForAzMap = new HashMap<>();
        try {
            // 1. 获取数据中心信息
            List<String> lsitCentory = response.getValue();
            logger.info("获取数据中心信息{}" , lsitCentory);
            // 2. 如果不为ALL直接获取数据中心信息，否则获取全部数据中心信息
            if (!ALL.equals(flag)) {
                lsitCentory = new ArrayList<>();
                lsitCentory.add(az);
            }
            for (String s : lsitCentory) {
                String dc = s.split(",")[0];
                // 获取服务节点
                CatalogServicesRequest requet = CatalogServicesRequest.newBuilder().setDatacenter(dc)
                    .setToken(aclToken).build();
                Response<Map<String, List<String>>> mapService = consClient.getCatalogServices(requet);
                HealthServicesRequest requestService;
                if ((mapService != null) && (mapService.getValue() != null)) {
                    Map<String, List<String>> mapInfo = mapService.getValue();
                    logger.info("根据数据中心获取服务信息{}" , mapInfo);
                    List<String> serviceNameList = new ArrayList<>();
                    for (String serviceName : mapInfo.keySet()) {
                        if (consulServeList.contains(serviceName)) {
                            serviceNameList.add(serviceName);
                        }
                    }
                    //根据键值存储对应服务名称列表
                    serviceNameForAzMap.put(dc, serviceNameList);
                    //获取健康服务节点信息
                    requestService = HealthServicesRequest.newBuilder().setDatacenter(dc).setToken(aclToken).build();
                    for (String serviceNameKey : serviceNameList) {
                        List<HealthService> services = consClient
                            .getHealthServices(serviceNameKey, requestService).getValue();
                        logger.info("根据健康服务信息{}" , services.get(0).getService().getService());
                        //不在白名单中的服务直接跳过
                        if (!Arrays.asList(CONSUL_SERVICE_NAME).contains (services.get(0).getService().getService())) {
                            continue;
                        }
                        if (services.size() > 0) {
                            for (HealthService hs : services) {
                                if ((hs != null) && (hs.getService() != null)) {
                                    ConsulInfoFetcherVo serviceNodes = new ConsulInfoFetcherVo();
                                    String checkServiceName = hs.getService().getService();
                                    serviceNodes.setAzflag(dc);
                                    serviceNodes.setService(checkServiceName);
                                    serviceNodes.setAddress(hs.getService().getAddress());
                                    serviceNodes.setPort(hs.getService().getPort().toString());
                                    listNode.add(serviceNodes);
                                }
                            }
                        }
                    }
                }
            }
            //遍历键值（数据中心）
            for (String serviceAz : serviceNameForAzMap.keySet()) {
                //根据键值（数据中心）遍历获取对应服务列表信息
                for (String consulService1 : serviceNameForAzMap.get(serviceAz)) {
                    if (!("").equals(consulService1) && !("").equals(az)) {
                        String refushIp;
                        //遍历获取数据中心下所有服务节点信息
                        for (ConsulInfoFetcherVo consulInfoFetcherVo : listNode) {
                            //根据不同数据中心下不同服务节点信息判断是否为需要刷新服务
                            if (serviceAz.equals(consulInfoFetcherVo.getAzflag())) {
                                //将需要刷新服务节点信息添加到list
                                if (consulInfoFetcherVo.getService().equals(consulService1)) {
                                    refushIp = consulInfoFetcherVo.getAddress() + SPLIT_FLAG + consulInfoFetcherVo.getPort();
                                    consulList.add(refushIp);
                                }
                            }
                        }
                    }
                }
            }
            logger.info("根据入参获取所有需要刷新ip信息{}" , consulList);
        } catch (Exception e) {
            logger.error("ip和端口查询失败：{}", e);
            System.out.println("ip和端口查询失败：" + e.getMessage());
            System.exit(-1);
        }
        logger.info("方法{},ConsulInfo END" , STATEMENT);
        return consulList;
    }


    /**
     * 刷新服务内存信息
     *
     * <p>该方法通过调用指定地址和端口的服务接口，刷新内存中的服务信息。会记录执行过程和结果，
     * 包括成功和失败的情况。</p>
     *
     * @param address 服务地址
     * @param port 服务端口
     * @return Map<String, String> 包含错误信息的Map，key为"地址:端口"，value为错误消息。
     *         如果刷新成功则返回空Map，失败则包含错误信息。
     *
     * @throws Exception 当调用服务或处理响应时发生异常
     *
     * <p>执行流程：
     * 1. 拼接服务地址和端口
     * 2. 创建WebClient并调用服务接口
     * 3. 处理响应结果：
     *    - 成功：记录成功日志
     *    - 失败：记录错误原因并返回错误信息
     * 4. 设置60秒超时时间</p>
     *
     * <p>注意事项：
     * - 使用SPLIT_FLAG常量拼接地址和端口
     * - 使用WEBURL和ADDRESSPATH常量构建完整请求URL
     * - 会记录详细的执行日志</p>
     */
    //刷新内存
    public Map<String, String> refushServices(String address, String port) {
        logger.info("方法{},refushServices START" , STATEMENT);
        logger.info("request params:{}{}{}" , address , SPLIT_FLAG , port);
        String ipAddress = address + SPLIT_FLAG + port;
        Map<String, String> mapError = new HashMap<>();
        String result = "";
        try {
            // 创建webClient
            WebClient webClient = WebClient.create(WEBURL + ipAddress);
            // 调用服务
            Mono<String> response = webClient.get()
                .uri(ADDRESSPATH) // 根据IP调用服务，注意URL拼接可能需要调整
                .retrieve().bodyToMono(String.class).map(respon -> ipAddress + "-" + respon).onErrorResume(e -> {
                    mapError.put(ipAddress, e.getMessage());
                    return Mono.just(ipAddress); // 确保onErrorResume返回一个Mono对象
                });
            // 阻塞获取结果,超时时间设置60秒
            result = response.block(Duration.ofSeconds(60));
            if(result != null && !"".equals(result)) {
                String[] resultArray = result.split("-", 2);
                if (resultArray.length <= 1){
                    System.out.println("执行结果:" + result + "刷新失败！失败原因:" + mapError.get(ipAddress));
                    logger.info("执行结果:{},刷新失败！失败原因:{}", result ,mapError.get(ipAddress));
                    return mapError;
                }
                RefreshResult refreshResult = JSON.parseObject(resultArray[1], RefreshResult.class);
                //刷新成功
                if (mapError.isEmpty() && refreshResult.isResultFlag()) {
                    logger.info("执行结果:{}", result);
                    System.out.println(ipAddress + "success");
                    //200情况的刷新失败，即刷新接口返回失败的情况
                } else if(mapError.isEmpty() && !refreshResult.isResultFlag()){
                    System.out.println("执行结果:" + result + "刷新失败！失败原因:" + refreshResult.getErrorMsg());
                    logger.info("执行结果:{},刷新失败！失败原因:{}", result , refreshResult.getErrorMsg());
                    mapError.put(ipAddress, refreshResult.getErrorMsg());
                } else {//除200情况的刷新失败
                    System.out.println("执行结果:" + result + "刷新失败！失败原因:" + mapError.get(ipAddress));
                    logger.info("执行结果:{},刷新失败！失败原因:{}", result ,mapError.get(ipAddress));
                }
            }else {
                logger.info("执行结果:返回结果为空，刷新失败！");
                mapError.put(ipAddress, "返回结果为空，刷新失败！");
                return mapError;
            }
        } catch (Exception e) {
            logger.error("刷新失败！{}", e);
            System.out.println(ipAddress + "刷新失败！失败原因：" + e.getMessage());
            mapError.put(ipAddress, e.getMessage());
        }
        System.out.println("执行结果:" + result);
        logger.info("方法{},callServices END" , STATEMENT);
        return mapError;
    }
}
