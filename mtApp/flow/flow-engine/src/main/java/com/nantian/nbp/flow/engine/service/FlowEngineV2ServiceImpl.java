package com.nantian.nbp.flow.engine.service;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.ExceptionResHandler;
import com.nantian.nbp.flow.engine.service.api.ExecuteConfig;
import com.nantian.nbp.flow.engine.service.api.FeConstants;
import com.nantian.nbp.flow.engine.service.api.FlowEngineService;
import com.nantian.nbp.flow.engine.service.api.FlowResult;
import com.nantian.nbp.flow.engine.service.api.PbLogHandler;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.json.valid.ValidJsonUtils;
import com.nantian.nbp.flow.engine.service.core.FlowDispatch;
import com.nantian.nbp.flowengine.model.FlowContainer;
import com.nantian.nbp.saga.SagaTransitionHelper;
import com.nantian.nbp.utils.CalendarUtil;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.JSON_SCHEMA_FLAG_TRUE;
import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ENGINE_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.ON;
import static com.nantian.nbp.flow.engine.service.api.Constants.RECORD_STEP_NO_KEY;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.ERR_EXCEPTION_TYPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.F_TRAN_CODE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.JSON_SCHEMA_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.REQ_JSON_SCHEMA_TYPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.UNN_EXCEPTION_TYPE;
import static com.nantian.nbp.flow.engine.service.api.context.AtomResult.ATOM_RESULT_KEY;
import static com.nantian.nbp.log4j2.TranLogConstants.TRACE_GID;

/**
 * 一个流程引擎服务对象，作为流程引擎的主入口
 *
 * @author Administrator
 */
@Component
public class FlowEngineV2ServiceImpl implements FlowEngineService<HttpServletResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowEngineV2ServiceImpl.class);

    private final ExceptionResHandler exceptionResHandler;

    private final CacheClientApi cacheClientApi;

    private final PbLogHandler pbLogHandler;

    @Value("${MY_POD_NAME:default}")
    private String myPodName;

    public FlowEngineV2ServiceImpl(CacheClientApi cacheClientApi,PbLogHandler pbLogHandler, ExceptionResHandler exceptionResHandler) {
        this.cacheClientApi = cacheClientApi;
        this.pbLogHandler = pbLogHandler;
        this.exceptionResHandler = exceptionResHandler;
    }

    /**
     * 执行服务方法，用于处理业务事务
     *
     * @param tcc      内外交易映射
     * @param bizId    业务ID，标识当前业务流程的唯一性
     * @param inHeader 请求头信息，包含一些控制和路由相关的数据
     * @param inData   请求数据，包含本次业务请求的具体内容
     * @return 返回FlowResult对象，包含处理结果和可能的错误信息
     * 方法执行过程：
     * 1. 首先，通过缓存API获取事务代码转换对象（TranCodeConv）z`
     * 2. 如果转换对象（TranCodeConv）为空，则记录日志，并构造一个新的TranCodeConv对象，
     * 设置缺失的事务代码和渠道编号，记录错误日志，并抛出自定义异常FlowException，
     * 指示没有相应的内部处理对应此事务代码。
     * 3. 如果存在转换对象（TranCodeConv），则通过它构造业务流程主键（FlowKey）
     * 并从缓存中获取对应的事务消息和主流程配置。
     * 4. 根据是否存在流程配置和消息配置，以及业务需求，执行相应的业务处理流程。
     */
    @Override
    public FlowResult executeService(TranCodeConv tcc, String bizId, Map<String, String> inHeader, String inData,
                                     HttpServletResponse response) {
        // 构造业务种类与交易码对应关系，用于后续流程处理。
        FlowKey fk = tcc.createFlowKey();
        TranCode tranCodeObj = cacheClientApi.getTranCodeMsg(fk.toString());
        // 获取流程配置定义
        FlowContainer flowContainer = cacheClientApi.getFlowContainer(fk);
        // 构造执行配置实例，准备执行服务方法。
        ExecuteConfig config = new ExecuteConfig(tcc, inData, inHeader);
        config.setBizId(bizId);

        return executeService(tranCodeObj, config, flowContainer, response);
    }

    /**
     * 执行服务流程的方法
     *
     * @param tranCodeObj   事务代码对象
     * @param config        执行配置
     * @param flowContainer 主流程对象
     * @return 返回流程执行结果
     */
    @Override
    public FlowResult executeService(TranCode tranCodeObj, ExecuteConfig config, FlowContainer flowContainer,
                                     HttpServletResponse response) {
        // 计时器
        Long startTime = Timer.getStartTime();
        // 准备各类配置变量
        String chnlNo = config.getChnlNo();
        String fTranCode = config.getfTranCode();
        String bizType = config.getBizType();
        String tranCode = config.getTranCode();
        String tranDate = CalendarUtil.getDate();
        String tranTime = CalendarUtil.getTime();
        TranCodeConv tcc = config.getTranCodeConv();

        // 初始化业务ID、请求数据等变量
        String bizId = config.getBizId();
        String reqData = config.getReqData();
        String gTrace = null;
        if (!ObjectUtils.isEmpty(config.getReqHeader())) {
            gTrace = config.getReqHeader().get(TRACE_GID);
        }

        // 创建日志条目
        PbLog pbLog = createPbLog(chnlNo, bizId, bizType, fTranCode, tranCode, tranDate, tranTime, gTrace);
        // 初始化上下文
        FeContext feContext = null;
        // 创建新的流程结果对象
        FlowResult flowResult = new FlowResult(bizId);
        Map<String, String> reqHeader;
        try {
            LOGGER.info("fTranCode :[{}_{}]->[{}_{}]", chnlNo, fTranCode, bizType, tranCode);
            Long feContextStartTime = Timer.getStartTime();
            // 初始化上下文
            feContext = createFeContext(bizId, chnlNo, fTranCode, bizType, tranCode, tranCodeObj);
            initScopeInfo(feContext, config, pbLog, bizId, tranDate, tranTime);
            defUnpackJson(reqData, feContext);
            reqHeader = config.getReqHeader();
            if (Objects.nonNull(reqHeader)) {
                feContext.getInHeaderScope().putAll(reqHeader);
            }
            LOGGER.debug("createFEContext UseTime[{}ms]", Timer.getUsedTime(feContextStartTime));
            // 调用流程引擎并记录执行时间
            Long execStartTime = Timer.getStartTime();
            // jsonSchema输入校验
            checkJsonSchema(feContext.getInScope(), tranCodeObj);
            // 执行处理流程
            String resStr = executeMainFlow(feContext, flowContainer);
            LOGGER.debug("flow exec UseTime[{}ms]", Timer.getUsedTime(execStartTime));
            if (Objects.isNull(resStr)) {
                throw new FlowException(FlowException.EFLW001, fTranCode, "returnData is null", null);
            }
            flowResult.setRetType(RetType.SUCCESS);
            flowResult.setRes(resStr);
            flowResult.setOutHeader(feContext.getOutHeaderScope());
            flowResult.setOutScope(feContext.getOutScope());
            pbLog.setPbTime(String.valueOf(Timer.getUsedTime(startTime)));
        } catch (Exception e) {
            LOGGER.error(APP_ENGINE_RUN_ERR_KEY + "[{}_{}]->[{}_{}]执行过程中发生非预期异常,错误信息[{}]!", chnlNo, fTranCode,
                    bizType, tranCode, e.getMessage(), e);
            String res = "";
            if (!ObjectUtils.isEmpty(feContext)) {
                String errType;
                if (feContext.getSagaTransitionManager().isNotEmpty()) {
                    SagaTransitionHelper.trigger(feContext, Boolean.TRUE);
                    errType = ERR_EXCEPTION_TYPE;
                } else {
                    errType = UNN_EXCEPTION_TYPE;
                }
                res = exceptionResHandler.exceptionRes(bizId, e, tcc, response, errType, pbLog);
            }
            flowResult.setRes(res);
        } finally {
            pbLogHandler.handle(pbLog, feContext);
            SagaTransitionHelper.recode(feContext);
        }
        return flowResult;
    }

    /**
     * 根据模板执行处理逻辑
     * 此方法整合了模板名称的确定、规则结果处理以及流程处理逻辑
     *
     * @param feContext     上下文对象，封装了流程执行所需的业务数据
     * @param flowContainer 主流程对象，用于指导流程的执行逻辑
     * @return HandlerContext 更新后的上下文处理器对象
     */
    private String executeMainFlow(FeContext feContext, FlowContainer flowContainer) {
        FlowDispatch.handleFlow(flowContainer, feContext);
        PbScope<Object> out = feContext.getOutScope();
        return defPackJson(out);
    }

    private void checkJsonSchema(Map<String, Object> reqPbScope, TranCode tranCode) {
        String allJsonSchemaFlag = cacheClientApi.getSysCfg(JSON_SCHEMA_FLAG);
        if (!Objects.equals(JSON_SCHEMA_FLAG_TRUE, allJsonSchemaFlag)) {
            return;
        }
        String apiName = tranCode.getBusiType() + "_" + tranCode.getApiCode() + "_" + REQ_JSON_SCHEMA_TYPE;
        Api api = cacheClientApi.getApi(apiName);
        if (ObjectUtils.isEmpty(api)) {
            LOGGER.warn(APP_ENGINE_RUN_ERR_KEY + "[{}]-[{}],API接口配置为空!", tranCode.getBusiType(), tranCode.getApiCode());
        } else {
            ValidJsonUtils.validJsonSchema(reqPbScope, api.getJsInfo(), tranCode.getIsBlocked());
        }
    }

    private void initScopeInfo(FeContext feContext, ExecuteConfig config, PbLog pbLog, String uuid, String tranDate,
            String tranTime) {
        PbScope<Object> sysMap = feContext.getSysScope();
        TranCode tranCodeObj = feContext.getTranCodeObj();
        sysMap.put(FeConstants.CHNL_NO, config.getChnlNo());
        sysMap.put(F_TRAN_CODE, config.getfTranCode());
        sysMap.put(FeConstants.BIZ_TYPE, config.getBizType());
        sysMap.put(FeConstants.TRAN_CODE, config.getTranCode());
        sysMap.put(FeConstants.TRAN_TYPE, tranCodeObj.getTranType());
        sysMap.put(FeConstants.TRAN_DATE, tranDate);
        sysMap.put(FeConstants.TRAN_TIME, tranTime);
        sysMap.put(FeConstants.BIZ_ID, uuid);
        sysMap.put(FeConstants.PB_LOG, pbLog);
    }

    @SuppressWarnings("Unchecked")
    private void defUnpackJson(String receiveString, FeContext feContext) {
        Map<String, Object> reqMap = JsonUtil.strToMap(receiveString);
        PbScope<Object> inScope = feContext.getInScope();
        inScope.putAll(reqMap);
    }

    @SuppressWarnings("Unchecked")
    private String defPackJson(PbScope<Object> out) {
        out.remove(ATOM_RESULT_KEY);
        return JsonUtil.writeValueAsString(out);
    }

    private PbLog createPbLog(String chnlNo, String seqNum, String busiType, String ftranCode, String tranCode,
            String tranDate, String tranTime, String gTrace) {
        PbLog pbLog = new PbLog();
        pbLog.setTranDate(tranDate);
        pbLog.setTranTime(tranTime);
        pbLog.setBizId(seqNum);
        pbLog.setgTrace(gTrace);
        pbLog.setReqChnl(chnlNo);
        pbLog.setfTranCode(ftranCode);
        pbLog.setBusiType(busiType);
        pbLog.setTranCode(tranCode);
        pbLog.setServiceStatusList(new ArrayList<>());
        String enableRecordStepNo = cacheClientApi.getSysCfg(RECORD_STEP_NO_KEY);
        if(Objects.equals(ON,enableRecordStepNo)){
            pbLog.setStepNoList(new LinkedList<>());
        }
        pbLog.setLogPath(myPodName);
        return pbLog;
    }

    private FeContext createFeContext(String bizId, String chnlNo, String fTranCode, String bizType, String tranCode,
            TranCode tranCodeObj) {
        FeContext feContext = new FeContext(cacheClientApi);
        feContext.setFeTranCode(fTranCode);
        feContext.setBizType(bizType);
        feContext.setTranCode(tranCode);
        feContext.setTranCodeObj(tranCodeObj);
        SagaTransitionManager sagaTransitionManager = new SagaTransitionManager(bizId, chnlNo, fTranCode, bizType, tranCode);
        feContext.setSagaTransitionManager(sagaTransitionManager);
        return feContext;
    }
}
