package com.n.fbsp.atom.platform.seed.other.atom.mq.common;

import com.nantian.nbp.base.model.HttpConf;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.HTTP_CONF_FBSP;
import static com.nantian.nbp.flow.engine.service.api.Constants.APP_MQ_PRODUCER_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;


/**
 * 基于之前的修改版
 *
 * @author Administrator
 */
@Component
public class MqHttpSvcClientImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqHttpSvcClientImpl.class);
    private final RestTemplate restTemplate4Svc;
    @Resource
    private final RestTemplate restTemplate;
    @Resource
    private final CacheClientApi cacheClientApi;

    public MqHttpSvcClientImpl(@Qualifier("restTemplate4Svc") RestTemplate restTemplate4Svc, RestTemplate restTemplate,
            CacheClientApi cacheClientApi) {
        this.restTemplate4Svc = restTemplate4Svc;
        this.restTemplate = restTemplate;
        this.cacheClientApi = cacheClientApi;
    }

    /**
     * 执行HTTP请求并处理响应的主流程
     *
     * @param taskInfo 请求任务的附加信息，用于丰富请求上下文
     * @return 返回HTTP响应中的主体内容为String类型，以便后续处理
     */
    public HttpEntity<String> exec(String chnlNo, String fTranCode, Map<String, Object> taskInfo) {
        //获取HTTP服务配置信息
        HttpConf httpConf = getHttpServiceConfig(HTTP_CONF_FBSP);
        // 获取HTTP客户端配置及请求实体准备
        HttpEntity<Object> requestEntity = prepareRequestEntity(taskInfo);
        // 获取服务名称
        String gatewayUrl = httpConf.getGatewayUrl();
        // 组合完整的请求URL
        String url = constructRequestUrl(chnlNo, fTranCode, gatewayUrl);
        HttpEntity<String> res;
        try {
            // 记录开始时间戳和结束时间戳用于计算用时。
            long startTime = System.currentTimeMillis();
            //发送http请求并处理异常,技术异常
            res = sendHttpRequest(gatewayUrl, url, requestEntity);
            // 记录结束时间戳。
            long endTime = System.currentTimeMillis();
            LOGGER.info("调用服务[{}]结束,调用地址[{}],开始时间[{}],结束时间[{}],用时[{}]ms", httpConf.getSerName(), url, startTime, endTime, endTime - startTime);
        } catch (FlowException e) {
            throw new FlowException(e.getMessage());
        }
        return res;
    }

    /**
     * 根据服务名从缓存中获取HTTP服务配置信息。
     * 此方法首先尝试从缓存中获取与参数中服务名对应的服务实例配置信息对象，
     * 如果获取成功，则返回该实例；如果获取失败，则抛出异常，表示没有找到相应的服务配置。
     *
     * @param serviceName 要获取其配置的服务名。
     * @return 返回HttpConf对象，包含服务的配置信息。
     */
    private HttpConf getHttpServiceConfig(String serviceName) {
        // 尝试从缓存中获取与参数中服务名对应的服务实例配置信息对象引用地址值赋值给局部变量httpConf以供后续处理或返回。
        HttpConf httpConf = cacheClientApi.getHttpConf(serviceName);
        // 判断是否成功获取到了HTTP配置信息对象实例引用地址值，如果没有，则执行异常处理逻辑部分代码段落。
        if (httpConf == null) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "MQ通信异常,未获取到服务名为[{}]的配置信息", serviceName);
            // 这个异常表示在请求服务信息过程中出现了流程执行失败的异常情况，具体原因为没有找到与服务名对应的服务实例配置信息。
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到服务名为[{}]的配置信息", serviceName), null);
        }
        return httpConf;
    }


    /**
     * 构建请求URL方法
     * 根据传入的参数构建请求的URL，并记录日志
     *
     * @param gatewayUrl 服务名称，用于构成URL的主机部分
     * @return 构建好的请求URL字符串
     */
    private String constructRequestUrl(String chnlNo, String fTranCode, String gatewayUrl) {
        // 构建请求URI
        String url = "http://" + gatewayUrl + "/webapi/tran/fbsp/" + chnlNo + "/" + fTranCode;
        LOGGER.info("send url [{}]", url);
        // 返回完整的请求URL字符串
        return url;
    }

    /**
     * 准备请求实体方法
     * 此方法主要用于根据请求类型和请求数据，生成Http请求实体对象
     *
     * @param taskInfo 包含请求类型和请求数据的映射表，用于准备HTTP请求实体对象。
     * @return 根据请求类型和操作需要，返回适当的HttpEntity对象
     */
    private HttpEntity<Object> prepareRequestEntity(Map<String, Object> taskInfo) {
        try {
            // 处理报文头参数
            HttpHeaders headers = new HttpHeaders();
            Map<String, String> headerMap = setBeforeHeader(taskInfo);
            LOGGER.debug("send httpHeader [{}]", JsonUtil.objToString(headerMap));
            mapToHttpHeaders(headers, headerMap);
            // 根据请求方法构建请求实体
            LOGGER.debug("send message [{}]", JsonUtil.objToString(taskInfo));
            // 对于POST请求，返回包含请求体和头部信息的HttpEntity对象
            return new HttpEntity<>(taskInfo, headers);
        } catch (Exception e) {
            LOGGER.error(APP_MQ_PRODUCER_ERR_KEY + "MQ通信异常,准备HttpEntity异常 [{}]", e.getMessage(), e);
            // 这个异常表示在请求服务信息过程中出现了流程执行失败的异常情况，具体原因为没有找到与服务名对应的服务实例配置信息。
            throw new FlowException(T_X0005091.getCode(), T_X0005091.getCodeMsg(e.getMessage()), e);
        }
    }


    /**
     * 将Map中的键值对添加到HttpHeaders对象中
     * 此方法的目的是将表示头部信息的Map<String, String>对象中的每一个键值对，
     * 添加到HttpHeaders对象中，以便在HTTP请求中使用这些头部信息。
     *
     * @param headers   要添加头部信息的HttpHeaders对象
     * @param headerMap 包含头部信息的Map对象，键值对表示HTTP头部字段和对应的值
     */
    private void mapToHttpHeaders(HttpHeaders headers, Map<String, String> headerMap) {
        // 检查headerMap是否非空
        if (Objects.nonNull(headerMap)) {
            // 使用forEach方法将headerMap中的每一对键值对添加到headers中。
            headerMap.forEach(headers::add);
        }
    }

    /**
     * 发送HTTP请求并处理异常方法
     * 此方法负责发送HTTP请求，并在发送请求时处理可能发生的异常
     * 它使用了Spring的RestTemplate来执行HTTP请求，并根据预定义的逻辑处理异常
     *
     * @param svcName       服务名称，用于选择合适的RestTemplate
     * @param url           目标URL地址
     * @param requestEntity 请求体，包含要发送的数据
     * @return HttpEntity<String> 返回响应主体
     */
    private HttpEntity<String> sendHttpRequest(String svcName, String url, HttpEntity<Object> requestEntity) {
        RestTemplate rest;
        // 根据服务名选择合适的RestTemplate实例
        if (svcName.contains(":")) {
            rest = restTemplate;
        } else {
            rest = restTemplate4Svc;
        }
        HttpEntity<String> res;
        // 尝试发送HTTP请求，若遇到异常则进行重试
        try {
            res = rest.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            /*
            表示客户端请求错误（如请求格式错误等），对应HTTP错误码为4xx。
            */
            throw new FlowException(e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            /*
            表示服务器错误（即服务器内部错误），对应HTTP错误码为5xx。
            */
            throw new FlowException(e.getResponseBodyAsString());
        } catch (Exception e) {
            /*
            其他: 请求因其他外部因素失败，例如目标资源的API速率限制、目标站点的防火墙策略等。
            */
            throw new FlowException(e.getMessage());
        }
        return res;
    }

    /**
     * 设置请求头的前置信息
     *
     * @param body 消息体内容，本参数未直接用于此方法，但注释假定其某种程度上影响方法执行或决策过程
     * @return 包含关键HTTP请求头的Map<String, String>实例，准备在即将发出的HTTP请求中使用这些头信息，
     */
    private Map<String, String> setBeforeHeader(Map<String, Object> body) {
        Map<String, String> headerMap = new HashMap<>();
        /*
        必填，取值为消息体的长度
        */
        byte[] data = body.toString().getBytes();
        headerMap.put("Content-Length", Integer.toString(data.length));
        /*
        必填，取值为：application/json;charset=utf-8；
        */
        headerMap.put("content-type", "application/json;charset=UTF-8");
        /*
         可选，取值为：close或者Keep-Alive，客户端支持http1.1默认为长连接。
         */
        headerMap.put("Connection", "Keep-Alive");
        return headerMap;
    }

}
