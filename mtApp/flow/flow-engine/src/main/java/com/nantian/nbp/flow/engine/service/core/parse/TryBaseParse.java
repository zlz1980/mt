/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.flow.engine.service.api.FeConstants;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.exception.PbRunTimeException;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.TryModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.FeConstants.ERROR_TRAN_STATUS;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.PB_LOG;

/**
 * Switch语法处理
 *
 * @author Administrator
 */
public class TryBaseParse extends BaseParseStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TryBaseParse.class);

    private static final String CATCH_IGN_ROLLBACK = "IGNROLLBACK";

    @Override
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (model instanceof TryModel) {
            TryModel tryModel = (TryModel) model;
            normalHandle(flowStrand, tryModel, atomResult);
        } else {
            throwAstTypeException("TryModel", model.getType());
        }
        return atomResult;
    }

    private void normalHandle(FlowStrand flowStrand, TryModel tryModel, AtomResult atomResult) {
        FlowUnit flowUnit = tryModel.getUnit();
        String curStep = flowStrand.getCurStep();
        LOGGER.info("##Step[{}] Execute TryModel[{}]Begin##", curStep, flowUnit.getAtomTranCode());
        LOGGER.info("AtomTran note[{}]", flowUnit.getNote());
        Long startTime = Timer.getStartTime();
        AstList tryList = tryModel.getTryBranch();
        FeContext feContext = flowStrand.getFeContext();
        PbScope<Object> tran = feContext.getTranScope();
        try {
            atomResult.copyValue(execList(flowStrand, tryList));
        } catch (PbRunTimeException e) {
            LOGGER.info("exec Catch errStep[{}] errCode[{}] errMsg[{}]", e.getErrStep(), e.getErrCode(), e.getMsg());
            tran.put(FeConstants.ERR_CODE, e.getErrCode());
            tran.put(FeConstants.ERR_MSG, e.getMsg());
            SagaTransitionManager sagaTransitionManager = feContext.getSagaTransitionManager();
            AstModel catchUnit = tryModel.getCatchUnit();
            /*发送短信等子流程内部的TRY/CATCH捕获后可以通过IGNROLLBACK参数设置控制不触发分布式事务回滚*/
            if (Objects.nonNull(sagaTransitionManager) && !CATCH_IGN_ROLLBACK.equalsIgnoreCase(catchUnit.getAtomTranParam())) {
                LOGGER.info("catch分支原子交易参数未设置IGNROLLBACK标识,系统默认将SAGA事务回滚标识设置为true");
                sagaTransitionManager.setTriggerFlag(Boolean.TRUE);
                PbScope<Object> sysMap = feContext.getSysScope();
                // 获取日志对象
                PbLog pbLog = (PbLog) sysMap.get(PB_LOG);
                pbLog.setTranStatus(ERROR_TRAN_STATUS);
            }
            AstList catchList = tryModel.getCatchBranch();
            atomResult.copyValue(execList(flowStrand, catchList));
        } finally {
            tran.remove(FeConstants.ERR_CODE);
            tran.remove(FeConstants.ERR_MSG);
        }
        flowStrand.setCurStep(tryModel.getEndStep());
        String endStep = flowStrand.getCurStep();
        LOGGER.info("##Step[{}] EndTryModel[{}]End!UseTime[{}]ms", endStep, flowUnit.getAtomTranCode(), Timer.getUsedTime(startTime));
    }

}
