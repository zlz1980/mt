package com.n.fbsp.atom.platform.seed.comm;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.HttpConf;
import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.ServiceStatus;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.ev.PbElExpressionParser;
import com.nantian.nbp.ev.PbEvContext;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.HttpSvcClientTemplate;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.context.SagaAtomResult;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.saga.AbstractSagaAtomService;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.saga.SagaRollbackStep;
import com.nantian.nbp.utils.CalendarUtil;
import com.nantian.nbp.utils.DateUtil;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_COMMA_SPILT;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_CONN_SPILT;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_THREE;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.URL_SPLIT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_START_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.MAP_INIT;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.PB_LOG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003094;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_N0001092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_N0001093;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_N0001094;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;
import static com.nantian.nbp.utils.Constants.CTX_FLAG;
import static com.nantian.nbp.utils.Constants.PARAM_NUM_TWO;

/**
 * 通讯服务,通过原子交易参数配置从缓存中加载与第三方通讯的服务配置信息，
 * 配置信息来自t_pb_ext_http_conf表，通过增强处理加载与其他服务的
 * HTTP请求报文体参数和HTTP请求头参数
 *
 * @author Administrator
 */
@Atom("base.ServiceComm")
public class HttpSvcClientImpl extends AbstractSagaAtomService {

    public static final String PATTERN_12 = "HH:mm:ss.SSS";
    public static final String PATTERN_23 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String ERR_SVR_NAME = "errSvrName";
    public static final String SVR_RESP_CODE = "svrRespCode";
    public static final String SVR_RESP_DESC = "svrRespDesc";
    public static final String BUSI_RET_TYPE = "busiRetType";
    public static final String HTTP_HEADER = "_httpHeader";

    //应用网关自身产生的异常应答返回码路径信息
    public static final String STD_RET_CODE_PATH = "${res.head.returnCode}";

    //应用网关自身产生的异常应答错误码路径信息
    public static final String STD_ERR_CODE_PATH = "${res.error.code}";

    //应用网关自身产生的异常应答错误信息路径信息
    public static final String STD_ERR_MSG_PATH = "${res.error.message}";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSvcClientImpl.class);
    private final RestTemplate restTemplate4Svc;
    private final RestTemplate restTemplate;
    private final CacheClientApi cacheClientApi;

    public HttpSvcClientImpl(@Qualifier("restTemplate4Svc") RestTemplate restTemplate4Svc, RestTemplate restTemplate,
                             CacheClientApi cacheClientApi) {
        this.restTemplate4Svc = restTemplate4Svc;
        this.restTemplate = restTemplate;
        this.cacheClientApi = cacheClientApi;
    }

    /**
     * 执行Saga原子交易
     * 1.原子交易参数（以 | 分割）：
     * 请求类型|服务名|URI
     * 请求类型:GET/POST
     * 服务名:AE基本定制-通讯处理模块中的服务名
     * URI:请求接口对应的具体URI信息
     * <p>
     * 2.增强处理参数（以 | 分割）：
     * 请求参数Key1|请求参数Value1
     * 请求参数Key2|请求参数Value2
     * 请求参数Key3|请求参数Value3
     * 请求参数Keyn|请求参数Valuen
     * <p>
     * 3.错误码及错误描述：
     * 强制配置错误码及错误描述
     *
     * @param tranContext
     * @param scopeValUnit 作用域单元，包含系统作用域和交易作用域
     * @param flowUnit     流程单元，包含Saga流程的配置信息
     * @return 返回Saga原子交易结果对象
     */
    @Override
    public AtomResult doService(TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit) {
        SagaAtomResult result = new SagaAtomResult();
        PbScope<Object> sysMap = tranContext.getSysScope();
        PbScope<Object> tranMap = tranContext.getTranScope();
        String fTranCode = tranContext.getFeTranCode();
        // 获取日志对象
        PbLog pbLog = (PbLog) sysMap.get(PB_LOG);
        //校验错误信息错误码是否配置
        PbCommonAtomUtils.validateErrInfo(flowUnit, result);
        // 获取原子交易参数配置
        String[] atomTranParams = getAtomTranParams(tranContext, flowUnit, result);
        //获取HTTP请求方法
        String methodType = atomTranParams[0];
        // 将参数字符串根据分隔符拆分成字符串数组
        String[] httpConfNames = getHttpConfNames(tranContext, atomTranParams[1], result);
        // 获取HTTP服务配置信息
        HttpConf httpConf = getHttpServiceConfig(httpConfNames[0].trim(), fTranCode, result);
        // 获取HTTP处理bean
        HttpSvcClientTemplate<Map<String, Object>> bean = httpConf.getClientTemplate();
        // 初始化服务状态对象
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setCurrStep(flowUnit.getAtomTranNo().toString());
        serviceStatus.setSerName(httpConf.getSysAbbr());
        // 执行统一的增强处理参数赋值
        FlowParaUtils.setFlowParaValues(tranContext, scopeValUnit, flowUnit);
        // 获取HTTP客户端配置及请求实体准备
        HttpEntity<Object> requestEntity = prepareRequestEntity(methodType, tranContext, scopeValUnit, flowUnit, bean, result);
        // 获取服务名称
        String gatewayUrl = httpConf.getGatewayUrl();
        // 组合完整的请求URL
        String reqUrl = constructRequestUrl(tranContext, atomTranParams[2], gatewayUrl);
        try {
            Long startTimer = Timer.getStartTime();
            // 记录开始时间戳和结束时间戳用于计算用时。
            String startTime = DateUtil.getDayTimeStr(PATTERN_23);
            // 发送http请求并处理异常,技术异常
            HttpEntity<String> res = sendHttpRequest(gatewayUrl, reqUrl, methodType, requestEntity, serviceStatus, pbLog, fTranCode, result);
            // 处理返回报文
            handleResponse(tranContext, scopeValUnit, flowUnit, res, bean, result);
            // 业务结果识别
            checkBizResult(tranContext, scopeValUnit, httpConfNames, serviceStatus, result);
            // 服务耗时
            serviceStatus.setSerTime(Timer.getUsedTime(startTimer) + "ms");
            LOGGER.info("service Status [{}]", serviceStatus);
            if ((RetType.UNKNOWN == result.getRetType()) || RetType.SUCCESS == result.getRetType() && !(RetType.FAILED == result.getBusiRetType())) {
                regSagaTransitionManager(tranContext, scopeValUnit, flowUnit);
            }
            if (!(RetType.SUCCESS == result.getRetType() && RetType.SUCCESS == result.getBusiRetType())) {
                setSagaManagerTriggerFlag(tranContext, flowUnit);
            }
            // 记录结束时间戳。
            String endTime = DateUtil.getDayTimeStr(PATTERN_23);
            LOGGER.info("callService [{}] End! requestURL[{}],beginTime[{}],endTime[{}],UseTime[{}]ms,svcRespCode[{}],svcRespMsg[{}]", serviceStatus.getSerName(), reqUrl, startTime, endTime, Timer.getUsedTime(startTimer), serviceStatus.getSerCode(), serviceStatus.getSerMsg());
            pbLog.addServiceStatus(serviceStatus);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "通信原子交易执行异常,异常原因[{}]", e.getMessage());
            processUnKnowException(e, tranContext, scopeValUnit, flowUnit);
            return result;
        } finally {
            if (!ObjectUtils.isEmpty(result.getBusiRetType())) {
                MapUtils.setVal(tranMap, BUSI_RET_TYPE, result.getBusiRetType().getType());
            } else {
                //技术状态不为空，并且状态为失败，则认为业务状态也为失败。否则认为业务状态为未明
                if (!ObjectUtils.isEmpty(result.getRetType()) && RetType.FAILED.getType()
                        .equals(result.getRetType().getType())) {
                    MapUtils.setVal(tranMap, BUSI_RET_TYPE, result.getRetType().getType());
                } else {
                    MapUtils.setVal(tranMap, BUSI_RET_TYPE, RetType.UNKNOWN);
                }
            }
            MapUtils.setVal(tranMap, ERR_SVR_NAME, httpConf.getSysAbbr());
            MapUtils.setVal(tranMap, SVR_RESP_CODE, serviceStatus.getSerCode());
            MapUtils.setVal(tranMap, SVR_RESP_DESC, serviceStatus.getSerMsg());
            LOGGER.debug("tran作用域信息: busiRetType[{}],errSvrName[{}],svrRespCode[{}],svrRespDesc[{}]", result.getBusiRetType()
                    .getType(), httpConf.getSysAbbr(), serviceStatus.getSerCode(), serviceStatus.getSerMsg());
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return result;
    }

    /**
     * 注册Saga流程步骤
     * 此方法用于在满足某个条件时，注册一个Saga流程的回调步骤这个步骤用于准备进行回调时所需要的信息
     *
     * @param tranContext  管理Saga流程转换的管理器
     * @param scopeValUnit 用于获取交易代码的范围价值单位
     * @param flowUnit     流程单位，包含例如流程代码等信息
     */
    @Override
    protected void registerSagaStep(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        try {
            // 记录日志，标记事务执行位置
            LOGGER.debug("saga事务管理器登记回调信息开始,当前流程步骤[{}],作用域[{}]", flowUnit.getAtomTranNo(), flowUnit.getAscope());
            // 实例化一个Saga回调步骤对象
            SagaRollbackStep rollbackStep = new SagaRollbackStep();
            String sagaParam = StrUtils.trim(flowUnit.getSagaParam());
            // 设置回调步骤的外部交易码
            rollbackStep.setRbfTranCode(sagaParam);
            // 基于stepParam获取httpConf，获取过程涉及的参数合法性校验以及非空校验等均在前序过程已经完成，此处不再对相关参数进行非空等校验
            String[] stepParams = StringUtils.delimitedListToStringArray(flowUnit.getAtomTranParam(), PARAM_SPILT_FLAG);
            String[] svcNames = StringUtils.delimitedListToStringArray(stepParams[1], PARAM_COMMA_SPILT);

            //对于经分布式重网关调用服务方服务的场景，reqDate和innerBusiKey的获取方式约定基于重网关在httpConf中的配置为准！
            HttpConf httpConf = cacheClientApi.getHttpConf(svcNames[0].trim());
            String inParamJson = null;
            if (!ObjectUtils.isEmpty(httpConf)) {
                PbEvContext ctx = createEvContext(scopeValUnit);
                String reqDateField = httpConf.getReqDateFieldName();
                String innerBusiKeyField = httpConf.getInnerBusikeyFieldName();
                if (StringUtils.hasText(reqDateField) && StringUtils.hasText(innerBusiKeyField)) {
                    String reqDateEl = MapUtils.repElVal(reqDateField);
                    String innerBusiKeyEl = MapUtils.repElVal(innerBusiKeyField);
                    String reqDate = (String) PbElExpressionParser.getVal(reqDateEl, ctx).getVal();
                    String innerBusiKey = (String) PbElExpressionParser.getVal(innerBusiKeyEl, ctx).getVal();
                    String[] sagaParams = StringUtils.delimitedListToStringArray(sagaParam, PARAM_CONN_SPILT);
                    Map<String, Object> inParamMap = new HashMap<>(MAP_INIT);
                    inParamMap.put("reqDate", reqDate);
                    inParamMap.put("innerBusiKey", innerBusiKey);
                    inParamMap.put("reqChnl", sagaParams[0]);
                    inParamMap.put("fTranCode", sagaParams[1]);
                    inParamJson = JsonUtil.objToString(inParamMap);
                } else {
                    LOGGER.error(APP_ATOM_RUN_ERR_KEY + "HttpConf中未配置reqDateFieldName,innerBusiKeyFieldName字段,请检查!");
                    throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("HttpConf中未配置reqDateFieldName,innerBusiKeyFieldName字段,请检查!"), null);
                }
            }
            rollbackStep.setInParamJson(inParamJson);
            // 尝试向流程管理器添加回调步骤
            tranContext.getSagaTransitionManager().addStep(rollbackStep);
            LOGGER.debug("saga事务管理器登记回调信息完成");
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "saga事务管理器登记回调信息异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("saga事务管理器登记回调信息异常,异常信息[{}]", e.getMessage()), e);
        }
    }

    /**
     * 重写异常处理方法，用于在Saga事务中的特定异常发生时执行补偿操作
     *
     * @param e            异常对象，代表在执行过程中遇到的异常
     * @param tranContext
     * @param scopeValUnit 作用域值单元，用于管理Saga事务中各个参与者的状态和数据
     * @param flowUnit     流单元，表示Saga事务中的一个业务流程单元
     */
    @Override
    protected void exceptionProcess(Exception e, TranContext tranContext, ScopeValUnit scopeValUnit,
                                    FlowUnit flowUnit) {
        // 注册Saga事务的转换管理器，以便在异常发生时能够根据当前的状态和数据执行相应的补偿操作
        regSagaTransitionManager(tranContext, scopeValUnit, flowUnit);
    }

    /**
     * 创建事件上下文对象
     * <p>
     * 此方法用于初始化一个PbEvContext对象，并将给定的数据作为上下文变量设置进去
     * 主要用于在事件处理过程中传递数据
     *
     * @param data 包含事件数据的Map对象，其中键是数据的名称，值是数据的值
     * @return 返回初始化后的PbEvContext对象
     */
    private PbEvContext createEvContext(Map<String, Object> data) {
        PbEvContext ctx = new PbEvContext();
        // 设置事件上下文变量，以便在后续的事件处理中使用
        ctx.setVariable(CTX_FLAG, data);
        return ctx;
    }

    /**
     * 获取原子交易参数数组
     * <p>
     * 此方法用于解析并验证给定的原子交易参数是否有效，包括检查参数是否为空、是否已正确配置以及参数数量是否正确
     * 如果参数验证失败，则设置结果状态为失败，并抛出FlowException异常
     *
     * @param tranContext 交易上下文访问接口
     * @param flowUnit    流程单元，包含待验证的原子交易参数
     * @param result      用于存储方法执行结果的SagaAtomResult对象
     * @return 字符串数组，包含拆分后的原子交易参数
     *
     * @throws FlowException 如果原子交易参数为空、未正确配置或参数数量不为3，则抛出此异常
     */
    private String[] getAtomTranParams(TranContext tranContext, FlowUnit flowUnit, SagaAtomResult result) {
        // 获取前端交易代码
        String fTranCode = tranContext.getFeTranCode();
        // 检查原子交易参数是否为空或被初始化为默认值
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            // 如果参数为空或未配置，设置结果状态为失败
            result.setRetType(RetType.FAILED);
            // 记录错误日志
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            // 抛出异常，指示原子交易参数配置错误
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 原子交易参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串根据分隔符拆分成字符串数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 验证参数长度是否为3，不符合则抛出异常，表示原子交易参数个数不正确
        if (atomTranParams.length != PARAM_NUM_THREE) {
            // 设置结果状态为失败
            result.setRetType(RetType.FAILED);
            // 记录错误日志
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            // 抛出异常，指示原子交易参数数量错误
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }
        // 返回拆分后的原子交易参数数组
        return atomTranParams;
    }

    private String[] getHttpConfNames(TranContext tranContext, String httpConfName, SagaAtomResult result) {

        String[] httpConfNames = StringUtils.delimitedListToStringArray(httpConfName, PARAM_COMMA_SPILT);
        // 验证参数长度是否大于2，不符合则抛出异常，表示个数不正确
        if (httpConfNames.length > PARAM_NUM_TWO) {
            // 设置结果状态为失败
            result.setRetType(RetType.FAILED);
            // 记录错误日志
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "服务名个数大于2,请检查参数配置[{}]", httpConfName);
            // 抛出异常，
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("服务名个数大于2,请检查参数配置[{}]", httpConfName), null);
        }
        return httpConfNames;
    }


    /**
     * 根据服务名从缓存中获取HTTP服务配置信息。
     * 此方法首先尝试从缓存中获取与参数中服务名对应的服务实例配置信息对象，
     * 如果获取成功，则返回该实例；如果获取失败，则抛出异常，表示没有找到相应的服务配置。
     *
     * @param serviceName 要获取其配置的服务名。
     * @param fTranCode   操作代码，用于异常处理时的流程跟踪。
     * @return 返回HttpConf对象，包含服务的配置信息。
     *
     * @throws FlowException 如果未找到指定服务名的配置信息，将抛出此异常。
     */
    private HttpConf getHttpServiceConfig(String serviceName, String fTranCode, SagaAtomResult result) {
        // 尝试从缓存中获取与参数中服务名对应的服务实例配置信息对象引用地址值赋值给局部变量httpConf以供后续处理或返回。
        HttpConf httpConf = cacheClientApi.getHttpConf(serviceName);
        // 判断是否成功获取到了HTTP配置信息对象实例引用地址值，如果没有，则执行异常处理逻辑部分代码段落。
        if (ObjectUtils.isEmpty(httpConf)) {
            result.setRetType(RetType.FAILED);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到服务名为[{}]的httpConf缓存配置信息", serviceName);
            throw new FlowException(T_C0003092.getCode(), fTranCode, T_C0003092.getCodeMsg("未获取到服务名为[{}]的httpConf缓存配置信息", serviceName), null);
        }
        return httpConf;
    }


    /**
     * 构建请求URL方法
     * 根据传入的参数构建请求的URL，并记录日志
     *
     * @param tranContext 交易上下文中访问接口
     * @param gatewayUrl  服务名称，用于构成URL的主机部分
     * @return 构建好的请求URL字符串
     */
    private String constructRequestUrl(TranContext tranContext, String uriStr, String gatewayUrl) {
        // 构建请求URI
        String[] uris = StringUtils.delimitedListToStringArray(uriStr, URL_SPLIT_FLAG);
        StringJoiner uriBuilder = new StringJoiner(URL_SPLIT_FLAG);
        // 遍历URI片段并处理变量替换
        for (String uri : uris) {
            if (uri.contains(VAR_START_FLAG)) {
                uri = StrUtils.toStrDefBlank(tranContext.getCtxVal(uri).getVal());
            }
            uriBuilder.add(uri);
        }
        // 设置服务名称并构建URL字符串
        String url = "http://" + gatewayUrl + URL_SPLIT_FLAG + uriBuilder;
        LOGGER.info("send url [{}]", url);
        // 返回完整的请求URL字符串
        return url;
    }

    /**
     * 准备请求实体方法
     * 此方法主要用于根据请求类型和请求数据，生成Http请求实体对象
     *
     * @param methodType   请求方法类型，支持POST等不同类型
     * @param tranContext  交易上下文访问接口
     * @param scopeValUnit 当前作用域下的值单元
     * @param flowUnit     当前流程单元
     * @param bean         具体的Http服务客户端模板
     * @return 根据请求类型和操作需要，返回适当的HttpEntity对象
     */
    private HttpEntity<Object> prepareRequestEntity(String methodType, TranContext tranContext,
                                                    ScopeValUnit scopeValUnit, FlowUnit flowUnit, HttpSvcClientTemplate<Map<String, Object>> bean,
                                                    SagaAtomResult result) {
        try {
            if (methodType.equalsIgnoreCase(HttpMethod.POST.name())) {
                // 请求周期中，提前处理请求体参数信息
                bean.beforeBody(tranContext, scopeValUnit, flowUnit, scopeValUnit);
            }
            // 处理报文头参数
            HttpHeaders headers = new HttpHeaders();
            Map<String, String> headerMap = new LinkedHashMap<>();
            //先进行默认赋值，再判断AE上送的数据，AE的优先级高
            headerMap = bean.beforeHeader(tranContext, scopeValUnit, flowUnit, headerMap);
            //scopeValUnit中的HTTP_HEADER内容来源于应用开发的业务流程定义，借助这个临时节点标签传递HTTP头信息
            if (!ObjectUtils.isEmpty(scopeValUnit.get(HTTP_HEADER))) {
                Map<String, String> aeHeaderMap = (Map<String, String>) scopeValUnit.get(HTTP_HEADER);
                headerMap.putAll(aeHeaderMap);
                //删除屏蔽临时传值的HTTP_HEADER节点
                scopeValUnit.remove(HTTP_HEADER);
            }
            mapToHttpHeaders(headers, headerMap);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("请求报文-Headers信息:[{}]", JsonUtil.objToString(headerMap));
            }
            // 根据请求方法构建请求实体
            if (methodType.equalsIgnoreCase(HttpMethod.POST.name())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("请求报文-Body信息:[{}]", JsonUtil.objToString(scopeValUnit));
                }
                // 对于POST请求，返回包含请求体和头部信息的HttpEntity对象
                return new HttpEntity<>(scopeValUnit, headers);
            } else {
                // 对于非POST请求，返回包含空请求体和头部信息的HttpEntity对象
                return new HttpEntity<>(null, headers);
            }
        } catch (FlowException e) {
            result.setRetType(RetType.FAILED);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "准备HTTP请求实体异常 [{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("准备HTTP请求实体异常 [{}]", e.getMessage()), e);
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
     * 将HTTP请求头转换为键值对形式的Map
     *
     * @param headers HTTP请求头的集合
     * @return 返回一个Map对象，其中包含请求头的所有键值对信息如果输入参数为null，则返回null
     */
    private LinkedHashMap<String, String> httpHeadersToMap(HttpHeaders headers) {
        if (Objects.nonNull(headers)) {
            // 创建一个Map来存储请求头键值对初始化大小为headers的大小以提高效率
            LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
            // 遍历请求头，将键值对存储到Map中对于每个键值对，将值的集合转换为逗号分隔的字符串
            headers.forEach((k, v) -> headerMap.put(k, StringUtils.collectionToDelimitedString(v, PARAM_COMMA_SPILT)));
            return headerMap;
        }
        return null;
    }

    /**
     * 发送HTTP请求并处理异常方法
     * 此方法负责发送HTTP请求，并在发送请求时处理可能发生的异常
     * 它使用了Spring的RestTemplate来执行HTTP请求，并根据预定义的逻辑处理异常
     *
     * @param svcName       服务名称，用于选择合适的RestTemplate
     * @param url           目标URL地址
     * @param methodType    HTTP方法类型，如GET、POST等
     * @param requestEntity 请求体，包含要发送的数据
     * @param serviceStatus 请求服务的状态信息
     * @param pbLog         日志记录对象，用于日志记录
     * @param fTranCode     当前流程的外部交易码
     * @return HttpEntity<String> 返回响应主体
     */
    private HttpEntity<String> sendHttpRequest(String svcName, String url, String methodType,
                                               HttpEntity<Object> requestEntity, ServiceStatus serviceStatus, PbLog pbLog, String fTranCode,
                                               SagaAtomResult result) {
        RestTemplate rest;
        // 根据服务名选择合适的RestTemplate实例
        if (svcName.contains(":")) {
            rest = restTemplate;
        } else {
            rest = restTemplate4Svc;
        }
        ResponseEntity<String> res = null;
        try {
            // 尝试发送HTTP请求
            res = rest.exchange(url, HttpMethod.valueOf(methodType.toUpperCase()), requestEntity, String.class);
            result.setRetType(RetType.SUCCESS);
        } catch (HttpClientErrorException e) {
            /*
            表示客户端请求错误（如请求格式错误等），对应HTTP错误码为4xx。
            400 (Bad Request): 请求中有错误或错误的信息。 示例: 请求参数格式错误。
            401 (Unauthorized): 请求需要用户身份认证。 示例: 用户名和密码错误或者未提供。
            403 (Forbidden): 请求被禁止访问。 示例: 用户没有足够的权限访问请求的资源。
            404 (Not Found): 请求的URI不存在。 示例: 请求的页面或API端点不存在。
            405 (Method Not Allowed): 请求的方法不被允许。 示例: 请求方法（如PUT）不被API端点允许。
            406 (Not Acceptable): 请求的内容类型不被服务器接受。 示例: 客户端请求JSON格式的数据，但是服务器只发送XML格式的数据。
            408 (Request Timeout): 请求超时。 示例: 请求处理时间超过服务器设定的时间。
            409 (Conflict): 请求的资源状态冲突。 示例: 用户尝试更新一个已经被他人更新的资源。
            410 (Gone): 请求的资源在服务器上已经永久删除。 示例: 请求的资源被管理员永久删除。
            411 (Length Required): 请求缺少阅读主体。 示例: POST请求没有提供必须的主体内容。
            412 (Precondition Failed): 请求的前提条件失败。 示例: 请求中包含如果资源修改日期早于某个时间则进行更新，但资源的修改日期不符合条件。
            413 (Payload Too Large): 请求的负载太大。 示例: 文件上传请求的文件超出了服务器允许的最大大小。
            414 (URI Too Long): 请求的URI太长。 示例: 请求的URL包含了过多的参数或者过长的路径。
            415 (Unsupported Media Type): 请求的媒体类型不被服务器支持。 示例:
            请求的主体是服务器不支持的格式，比如服务器只支持XML但请求发送的是JSON。
            416 (Range Not Satisfiable): 请求的范围无法满足。 示例: 请求中包含的范围超出了服务器可以提供的范围。
            417 (Expectation Failed): 请求的期望值无法满足。 示例: 请求中包含的期望值（Expect）头部字段无法满足
            */
            result.setRetType(RetType.FAILED);
            handleException(e, T_N0001092.getCode(), T_N0001092.getCodeMsg("HTTP错误码[{}],异常原因[{}]", e.getStatusCode(), e.getMessage()), fTranCode, serviceStatus, pbLog);
        } catch (HttpServerErrorException e) {
            /*
            表示服务器错误（即服务器内部错误），对应HTTP错误码为5xx。
            500 (InternalServerError): 内部服务器错误。 示例: 请求处理过程中服务器发生未知错误。
            501 (Not Implemented): 请求的方法服务器不支持。 示例: 请求的方法（如HTTP协议的某个版本的某个特性）服务器不支持。
            502 (Bad Gateway): 请求的上游服务器返回了一个无效响应。 示例: 请求的代理服务器或网关收到一个无效响应。
            503 (Service Unavailable): 请求的服务当前不可用。 示例: 请求的服务由于维护或者过载而临时不可用。
            504 (Gateway Timeout): 请求的上游服务器没有及时响应。
            */
            result.setRetType(RetType.UNKNOWN);
            handleException(e, T_N0001093.getCode(), T_N0001093.getCodeMsg("HTTP错误码[{}],异常原因[{}]", e.getStatusCode(), e.getMessage()), fTranCode, serviceStatus, pbLog);
        } catch (Exception e) {
            /*
            资源访问异常
            网络连接问题: 请求的目标资源时网络连接中断或超时。
            权限问题: 请求因缺少必要的权限而被拒绝。
            资源临时不可用: 请求的资源临时不可用，例如数据库连接池已满、远程服务宕机等。
            维护窗口: 请求的资源在维护窗口内，人为下线或重启。
            安全策略: 请求因安全策略被拦截，例如安全组、网络ACL等。
            其他: 请求因其他外部因素失败，例如目标资源的API速率限制、目标站点的防火墙策略等。
            */
            // 其他HTTP请求异常
            result.setRetType(RetType.UNKNOWN);
            handleException(e, T_N0001094.getCode(), T_N0001094.getCodeMsg("异常原因[{}]", e.getMessage()), fTranCode, serviceStatus, pbLog);
        }
        return res;
    }

    /**
     * 处理返回码和消息示例方法（根据实际情况调整）
     * 对返回的实体进行处理，提取响应头和响应体，并对各种标志和状态进行设置
     * 此方法主要用途是处理HTTP请求后的响应，用于确定应用程序接下来的操作和状态更新
     * 参数列表包含处理响应所需的各类实例和配置，允许根据返回的响应灵活处理业务逻辑和服务状态更新
     *
     * @param tranContext  交易上下文访问接口
     * @param scopeValUnit 作用域值单元，用于存储和传播过程中的值
     * @param flowUnit     流程单元，在流程处理中传递信息
     * @param res          封装响应内容的HttpEntity对象
     * @param bean         HTTP服务客户端模板，允许执行后续的HTTP操作
     * @param result       存储过程结果，如注册回调标志和触发Saga信息标志等
     */
    private void handleResponse(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit,
                                HttpEntity<String> res, HttpSvcClientTemplate<Map<String, Object>> bean, SagaAtomResult result) {
        // 验证返回实体是否为空
        if (ObjectUtils.isEmpty(res)) {
            result.setRetType(RetType.UNKNOWN);
            result.setBusiRetType(RetType.UNKNOWN);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "响应报文为空");
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("响应报文为空"), null);
        }
        try {
            // 将响应头转换为Map对象
            LinkedHashMap<String, String> respHttpHeaders = httpHeadersToMap(res.getHeaders());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("应答报文-Headers信息:[{}]", JsonUtil.objToString(respHttpHeaders));
            }
            // 将响应体转换为Map对象
            Map<String, Object> response = JsonUtil.strToMap(res.getBody());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("应答报文-Body信息:[{}]", JsonUtil.objToString(response));
            }
            // 处理返回报文头
            bean.afterHeader(tranContext, scopeValUnit, flowUnit, respHttpHeaders);
            // 处理返回报文体
            bean.afterBody(tranContext, scopeValUnit, flowUnit, response);
            scopeValUnit.put("res", response);
        } catch (Exception e) {
            // 技术通讯成功的情况下，进行业务结果判断异常，此时认为需要进行saga异常处理
            result.setRetType(RetType.UNKNOWN);
            result.setBusiRetType(RetType.UNKNOWN);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "处理响应报文异常 [{}]", e.getMessage(), e);
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("处理响应报文异常 [{}]", e.getMessage()), e);
        }
    }

    private void checkBizResult(TranContext tranContext, ScopeValUnit scopeValUnit, String[] httpConfNames,
                                ServiceStatus serviceStatus, SagaAtomResult result) {
        try {
            HttpConf[] httpConfs = new HttpConf[2];
            PbEvContext ctx = createEvContext(scopeValUnit);
            httpConfs[0] = getHttpServiceConfig(httpConfNames[0].trim(), tranContext.getFeTranCode(), result);
            // 目标系统返回码和消息所存放栏位的路径信息，无论是否经EDSP转发，均以第一个服务配置为准
            String retCodeFieldName = httpConfs[0].getRetCodeFieldName();
            String retErrCodeFieldName = httpConfs[0].getRetErrCodeFieldName();
            String retErrMsgFieldName = httpConfs[0].getRetErrMsgFieldName();
            boolean chkRet = checkField(retCodeFieldName, retErrCodeFieldName, retErrMsgFieldName);
            if (!chkRet) {
                result.setBusiRetType(RetType.UNKNOWN);
                result.setErrCode(T_C0003094.getCode());
                result.setErrMsg(T_C0003094.getCodeMsg("httpConf服务[{}]配置数据缺失", httpConfNames[0]));
                return;
            }
            String retCodeEl = MapUtils.repElVal(retCodeFieldName);
            String retErrCodeEl = MapUtils.repElVal(retErrCodeFieldName);
            String retErrMsgEl = MapUtils.repElVal(retErrMsgFieldName);
            //从报文中获取应答码信息取值
            String retCodeVal = (String) PbElExpressionParser.getVal(retCodeEl, ctx).getVal();
            String retErrCodeVal = (String) PbElExpressionParser.getVal(retErrCodeEl, ctx).getVal();
            String retErrMsgVal = (String) PbElExpressionParser.getVal(retErrMsgEl, ctx).getVal();
            //如果基于HttpConf配置信息未获取到有效的应答码信息，则按照网关应答码进一步尝试获取
            if (!StringUtils.hasText(retCodeVal) && !STD_RET_CODE_PATH.equals(retCodeFieldName.trim())) {
                LOGGER.error("服务方应答报文中未获取到有效的应答信息，按照网关应答码配置尝试进一步获取应答码信息");
                String gwRetCodeEl = MapUtils.repElVal(STD_RET_CODE_PATH);
                String gwRetErrCodeEl = MapUtils.repElVal(STD_ERR_CODE_PATH);
                String gwRetErrMsgEl = MapUtils.repElVal(STD_ERR_MSG_PATH);

                retCodeVal = (String) PbElExpressionParser.getVal(gwRetCodeEl, ctx).getVal();
                retErrCodeVal = (String) PbElExpressionParser.getVal(gwRetErrCodeEl, ctx).getVal();
                retErrMsgVal = (String) PbElExpressionParser.getVal(gwRetErrMsgEl, ctx).getVal();
            }
            LOGGER.info("retCodeVal [{}]", retCodeVal);
            LOGGER.info("retErrCodeVal [{}]", retErrCodeVal);
            LOGGER.info("retErrMsgVal [{}]", retErrMsgVal);
            if (!StringUtils.hasText(retCodeVal)) {
                result.setBusiRetType(RetType.UNKNOWN);
                result.setErrCode(T_N0001093.getCode());
                result.setErrMsg(T_N0001093.getCodeMsg("应答码获取失败"));
                return;
            }
            if (httpConfNames.length == 2) {
                httpConfs[1] = getHttpServiceConfig(httpConfNames[1].trim(), tranContext.getFeTranCode(), result);
            }
            //成功状态仅需要依据实际服务方结果进行判定
            if (httpConfs[httpConfNames.length == 2 ? 1 : 0].getSuccessRetCodes().contains(retCodeVal)) {
                serviceStatus.setSerCode(retCodeVal);
                serviceStatus.setSerMsg(RetType.SUCCESS.getType());
                result.setBusiRetType(RetType.SUCCESS);
                return;
            }
            serviceStatus.setSerCode(retErrCodeVal);
            serviceStatus.setSerMsg(retErrMsgVal);
            //倒序for循环httpConfNames，先判定服务方返回码（服务方成功直接返回，最终再走EDSP判定）
            for (int i = httpConfNames.length - 1; i >= 0; i--) {
                // 根据返回码处理服务状态和结果标志
                if (httpConfs[i].getUnknownRetCodes().contains(retCodeVal)) {
                    result.setBusiRetType(RetType.UNKNOWN);
                    return;
                }
            }
            //未匹配成功和未明应答码的情况
            result.setBusiRetType(RetType.FAILED);
            result.setErrCode(retErrCodeVal);
            result.setErrMsg(retErrMsgVal);
        } catch (Exception e) {
            // 技术通讯成功的情况下，进行业务结果判断异常，此时认为需要进行saga异常处理
            result.setBusiRetType(RetType.UNKNOWN);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "处理响应报文异常 [{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("处理响应报文异常 [{}]", e.getMessage()), e);
        }
    }

    /**
     * 处理异常方法
     * 将错误信息封装到ServiceStatus服务状态对象，并抛出FlowException异常
     * <p>
     * 方法主要执行以下操作：
     * 1. 设置serviceStatus对象的错误代码和信息，以及服务执行时间；
     * 2. 调用日志工具类记录异常处理的日志；
     * 3. 使用错误信息和服务状态对象进行异常处理，并通过pbLog对象记录；
     * 4. 抛出自定义的FlowException异常，传递错误代码、事务代码和错误信息，以及原始异常对象。
     * <p>
     * 注意：本方法不直接返回任何数据或参与流程控制，其作用主要集中在异常处理和日志记录上。
     *
     * @param e             异常对象，表示发生的错误
     * @param errorCode     错误代码，用于记录和分类错误信息
     * @param errorMsg      错误描述信息，用于详细说明错误情况
     * @param fTranCode     流处理事务代码，可能对于调试和追踪有帮助
     * @param serviceStatus ServiceStatus对象，用于记录服务执行的状态信息
     * @param pbLog         日志对象，记录方法调用的日志信息
     * @throws FlowException 抛出的自定义异常，表示流程执行中出现的错误
     */
    private void handleException(Exception e, String errorCode, String errorMsg, String fTranCode,
                                 ServiceStatus serviceStatus, PbLog pbLog) {
        // 设置服务状态错误代码和信息
        serviceStatus.setSerCode(errorCode);
        serviceStatus.setSerMsg(errorMsg);
        // 设置服务执行时间，使用统一格式化日期字符串表示时间戳，如yyyy-MM-dd HH:mm:ss格式
        serviceStatus.setSerTime(CalendarUtil.getDate(PATTERN_12));
        // 输出服务状态的日志信息，使用日志记录工具类完成日志记录功能
        // 更新pbLog日志对象的服务状态信息字段，将当前服务状态对象添加到日志记录中
        pbLog.addServiceStatus(serviceStatus);
        LOGGER.debug("sendHttpRequest异常, 错误码 [{}], 错误信息 [{}]", errorCode, errorMsg);
        // 抛出自定义异常，包含错误代码、事务代码和错误信息，以及原始的异常对象e作为原因链式调用的参数
        throw new FlowException(errorCode, fTranCode, errorMsg, e);
    }

    private boolean checkField(String retCodeFieldName, String retErrCodeFieldName, String retErrMsgFieldName) {
        String errMsg = APP_ATOM_RUN_ERR_KEY + "httpConf 配置标签与应答报文标签不一致,请检查[{}]";
        if (!StringUtils.hasText(retCodeFieldName)) {
            LOGGER.error(errMsg, "retCodeFieldName");
            return false;
        }
        if (!StringUtils.hasText(retErrCodeFieldName)) {
            LOGGER.error(errMsg, "retErrCodeFieldName");
            return false;
        }
        if (!StringUtils.hasText(retErrMsgFieldName)) {
            LOGGER.error(errMsg, "retErrMsgFieldName");
            return false;
        }
        return true;
    }

}
