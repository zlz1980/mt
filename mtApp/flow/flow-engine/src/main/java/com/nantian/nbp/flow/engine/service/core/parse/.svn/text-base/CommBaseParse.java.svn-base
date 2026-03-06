/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.exception.PbRunTimeException;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ENGINE_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.IGNORE_EXCEPTION;
import static com.nantian.nbp.flow.engine.service.api.context.AtomResult.ATOM_RESULT_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 流程引擎通用解析器，负责解析和调度原子组件
 * @author Administrator
 */
public class CommBaseParse extends BaseParseStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommBaseParse.class);

    @Override
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (ASTType.COMM.equals(model.getType())) {
            return normalHandle(flowStrand, model, atomResult);
        }
        return atomResult;
    }

    /**
     * 标准组件处理调度
     * @param model 语法节点
     * @param atomResult 上一步返回结果
     * @return Result本节点结果
     * @throws FlowException 流程异常
     */
    private AtomResult normalHandle(FlowStrand flowStrand, AstModel model, AtomResult atomResult) {
        FlowUnit flowUnit = model.getUnit();
        String curStep = flowStrand.getCurStep();
        String atomTranCode = flowUnit.getAtomTranCode();
        LOGGER.info("##Step[{}] Execute atomTran[{}]Begin##", curStep, atomTranCode);
        LOGGER.info("AtomTran note[{}]", flowUnit.getNote());
        LOGGER.info("AtomTran param[{}]", flowUnit.getAtomTranParam());

        Long startTime = Timer.getStartTime();
        try {
            atomResult = invoke(flowStrand.getFeContext(), model, flowUnit);
        } catch (Exception e) {
            // Y-忽略异常标志, N代表正常不忽略
            String abnProcType = flowUnit.getAbnProcType();
            if (Objects.equals(IGNORE_EXCEPTION, abnProcType)) {
                LOGGER.warn("Ignore exceptions path[{}] abnProcType[{}]", curStep, abnProcType, e);
                return atomResult;
            }
            LOGGER.error(APP_ENGINE_RUN_ERR_KEY + "Err path[{}],err[{}]", curStep, e.getMessage());
            if (e instanceof PbRunTimeException || e instanceof FlowException) {
                throw e;
            }
            String errMsg = e.getMessage();
            throw new FlowException(T_X0005091.getCode(), flowStrand.getFeTranCode(), T_X0005091.getCodeMsg(errMsg), curStep, atomTranCode, e);
        } finally {
            LOGGER.info("##Step[{}] atomTran[{}]End!ReturnFlag[{}]!UseTime[{}]ms", curStep, atomTranCode, atomResult.isSuccess(), Timer.getUsedTime(startTime));
        }
        return atomResult;
    }

    /**
     *
     * 调用前处理
     * @param feContext 上下文对象
     * @param flowUnit 流程单元
     * @return Result结果
     * @throws FlowException 流程异常
     */
    private AtomResult invoke(FeContext feContext, AstModel model, FlowUnit flowUnit) throws FlowException {
        AtomService atomService = model.getAtomService();
        if (Objects.isNull(atomService)) {
            throw new FlowException("bean is null");
        }
        LOGGER.debug("Invoke bean name is [{}]", flowUnit.getAtomTranCode());
        return beanInvoke(atomService, feContext, flowUnit);
    }

    /**
     * Spring bean调用
     *
     * @param bean AtomService bean
     * @param feContext 当前上下文
     * @param flowUnit  当前流程配置
     * @return Result结果
     * @throws FlowException          流程异常
     */
    private AtomResult beanInvoke(AtomService bean, FeContext feContext, FlowUnit flowUnit) {
        ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext,flowUnit);
        AtomResult atomResult = new AtomResult(RetType.UNKNOWN);
        String errCode = (String) feContext.getValue(StrUtils.toStr(flowUnit.getErrCode(), ZERO_STR));
        try {
            atomResult = bean.doService(feContext, scopeValUnit, flowUnit);
        } catch (Exception e) {
            atomResult.setErrCode(errCode);
            atomResult.setErrMsg(e.getMessage());
            throw e;
        } finally {
            scopeValUnit.put(ATOM_RESULT_KEY, atomResult.resToPbScope());
        }
        if (!atomResult.isSuccess() && !Objects.equals(ZERO_STR, errCode)) {
            String errInfo = (String) feContext.getValue(StrUtils.toStrDefBlank(flowUnit.getErrorInfo()));
            throw new PbRunTimeException(errCode, errInfo);
        }
        return atomResult;
    }

}
