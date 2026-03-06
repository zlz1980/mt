package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flow.engine.service.api.util.json.valid.ValidJsonUtils;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.AsyncModuleModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.nantian.nbp.cache.server.api.Constants.JSON_SCHEMA_FLAG_TRUE;
import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ENGINE_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.JSON_SCHEMA_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.MAP_INIT;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.REQ_JSON_SCHEMA_TYPE;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 异步处理模块语法处理
 *
 * @author Administrator
 */
@Component
public class AsynBaseParse extends BaseParseStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynBaseParse.class);
    /**
     * 自定义线程池配置
     */
    ExecutorService executor = new ThreadPoolExecutor(
            // 核心线程数
            4,
            // 最大线程数
            8,
            // 空闲线程存活时间
            60,
            // 单位秒
            TimeUnit.SECONDS,
            // 队列容量
            new LinkedBlockingQueue<>(100),
            // 拒绝策略,当前主线程自己执行任务
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (model instanceof AsyncModuleModel) {
            AsyncModuleModel asyncModuleModel = (AsyncModuleModel) model;
            normalHandle(flowStrand, asyncModuleModel);
        } else {
            throwAstTypeException(ASTType.ASYNMODULE.getType(), model.getType());
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    /**
     * 处理流程的核心方法
     * 该方法负责执行流程中的某个步骤，并处理相应的原子事务
     * 主要包括以下几个阶段：
     * 1. 初始化环境和日志记录
     * 2. 获取执行单元和上下文对象
     * 3. 初始化并执行处理模块，包括子步骤列表
     * 4. 处理结果并更新原始流程状态
     *
     * @param flowStrand       流程链对象，包含与当前步骤相关的信息和状态
     * @param asyncModuleModel 异步模型对象，封装了当前处理步骤的配置和参数
     */
    private void normalHandle(FlowStrand flowStrand, AsyncModuleModel asyncModuleModel) {
        // 初始化计时器，用于记录执行时间
        Long startTime = Timer.getStartTime();
        // 获取当前执行单元和流程链对象
        FlowUnit flowUnit = asyncModuleModel.getUnit();
        // 设置当前步骤ID到流程链对象中
        String curStep = flowStrand.getCurStep();
        String atomTranParam = flowUnit.getAtomTranParam();
        // 记录步骤开始日志
        LOGGER.info("##Step[{}] Execute Async Module [{}] Begin##", curStep, atomTranParam);
        LOGGER.info("AtomTran note[{}]", flowUnit.getNote());
        // 获取处理模块内步骤list
        AstList childrenList = asyncModuleModel.getChildrenList();
        // 创建模块的上下文对象
        FeContext feContext = flowStrand.getFeContext();
        TranCode modelTranCode = asyncModuleModel.getTranCode();
        // 获取模块请求参数
        ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext, flowUnit);
        FlowParaUtils.setFlowParaValues(feContext, scopeValUnit, flowUnit);

        // 验证请求参数的JSON Schema
        boolean checkResult = checkReqJsonSchema(feContext, modelTranCode, scopeValUnit);
        if (!checkResult) {
            return;
        }
        // 创建新的上下文对象用于模型
        FeContext modelFeContext = createNewFeContext(feContext, asyncModuleModel, scopeValUnit);
        // 初始化子流程链对象
        FlowStrand modelFlowStrand = new FlowStrand(atomTranParam, FlowStrand.SUB_PROCESS, modelFeContext);
        modelFlowStrand.setParentStep(curStep);
        LOGGER.info("##Step[{}] AsyncModule[{}]End!UseTime[{}]ms", curStep, atomTranParam, Timer.getUsedTime(startTime));
        // 复制当前线程的MDC到子线程
        Map<String, String> parentMdc = MDC.getCopyOfContextMap();
        // 异步执行子步骤列表
        executor.execute(() -> {
            try {
                // 设置子线程的MDC
                MDC.setContextMap(parentMdc);
                execAsyncModule(modelFlowStrand, childrenList);
            } finally {
                // 执行完成后清理MDC（避免资源泄漏）
                MDC.clear();
            }
        });
    }

    /**
     * 方法描述：重新创建上下文对象并返回。
     * <p>
     * 此方法旨在处理与异步模型相关的上下文对象创建流程。它首先从给定的异步模型中提取传输代码，
     * 然后获取与特定模块相关的请求参数，对这些参数进行校验，最终构造并返回一个新的上下文对象。
     *
     * @param feContext        表示当前已经存在的上下文对象，包括系统信息、事务信息等。
     * @param asyncModuleModel 异步模型对象，其中包含的传输代码有助于确定请求的性质和处理方式。
     * @return 返回重新创建的FeContext对象，包含了更新后的系统和事务信息。
     */
    private FeContext createNewFeContext(FeContext feContext, AsyncModuleModel asyncModuleModel,
            ScopeValUnit scopeValUnit) {
        TranCode modelTranCode = asyncModuleModel.getTranCode();
        // 参数赋值，创建了一个新的FeContext实例，使用原始和目标上下文的信息以及校验后的参数
        PbScope<Object> tranMap = new PbScope<>(MAP_INIT);
        tranMap.putAll(feContext.getTranScope());
        FeContext modelFeContext = new FeContext(feContext.getSysScope(), tranMap, scopeValUnit, feContext.getCacheClientApi());
        modelFeContext.setFeTranCode(feContext.getFeTranCode());
        modelFeContext.setBizType(modelTranCode.getBusiType());
        modelFeContext.setTranCode(modelTranCode.getTranCode());
        modelFeContext.setTranCodeObj(modelTranCode);
        modelFeContext.setSagaTransitionManager(feContext.getSagaTransitionManager());
        return modelFeContext;
    }

    /**
     * 校验请求的JSON格式是否符合规范
     * 此方法主要用于检查通过模型转换而来的请求参数是否符合预期的JSON模式
     * 它依赖于上下文中的缓存API来获取系统配置，并使用验证工具类进行模式校验
     *
     * @param feContext     上下文对象，用来获取缓存API和承载转换模型
     * @param modelTranCode 请求的转换模型代码，包含请求的JSON信息
     * @param pbScope       请求的protobuf作用域，用于校验JSON模式的protobuf定义
     */
    private boolean checkReqJsonSchema(FeContext feContext, TranCode modelTranCode, PbScope<Object> pbScope) {
        String allJsonSchemaFlag = feContext.getCacheClientApi().getSysCfg(JSON_SCHEMA_FLAG);
        if (!Objects.equals(JSON_SCHEMA_FLAG_TRUE, allJsonSchemaFlag)) {
            return true;
        }
        try {
            String apiName = modelTranCode.getBusiType() + "_" + modelTranCode.getApiCode() + "_" + REQ_JSON_SCHEMA_TYPE;
            Api api = feContext.getCacheClientApi().getApi(apiName);
            if (ObjectUtils.isEmpty(api)) {
                LOGGER.warn("[{}]-[{}],API接口配置为空!", modelTranCode.getBusiType(), modelTranCode.getTranCode());
                return true;
            }
            ValidJsonUtils.validJsonSchema(pbScope, api.getJsInfo(), modelTranCode.getIsBlocked());
        } catch (Exception e) {
            LOGGER.warn("JSON校验开关开启，异步调用输入数据格式校验失败，请检查接口规范配置!", e);
            return false;
        }
        return true;
    }

    /**
     * 异步模块执行方法
     * 该方法旨在处理异步模块的执行，通过捕获异常来确保异步执行过程的稳定性
     *
     * @param modelFlowStrand 流模型的执行线程，用于管理异步执行的上下文
     * @param childrenList 子节点列表，包含需要执行的异步任务
     */
    private void execAsyncModule(FlowStrand modelFlowStrand, AstList childrenList) {
        try {
            // 执行子节点列表中的所有任务
            this.execList(modelFlowStrand, childrenList);
        } catch (Exception e) {
            // 记录异步模块执行中的异常信息
            LOGGER.error(APP_ENGINE_RUN_ERR_KEY + "异步模块执行异常,异常信息[{}]", T_X0005091.getCodeMsg(e.getMessage()), e);
        }
    }
}
