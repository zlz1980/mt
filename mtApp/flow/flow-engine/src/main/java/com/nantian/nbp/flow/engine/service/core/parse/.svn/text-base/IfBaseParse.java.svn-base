/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.IfModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If语法处理
 * @author Administrator
 */
public class IfBaseParse extends BaseParseStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(IfBaseParse.class);

    @Override
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (model instanceof IfModel) {
            IfModel ifModel = (IfModel) model;
            normalHandle(flowStrand, ifModel, atomResult);
        }else {
            throwAstTypeException(ASTType.IF.getType(),model.getType());
        }
        return atomResult;
    }

    private void normalHandle(FlowStrand flowStrand, IfModel ifModel, AtomResult atomResult) {
        FlowUnit flowUnit = ifModel.getUnit();
        String curStep = flowStrand.getCurStep();
        LOGGER.info("##Step[{}] Execute If[{}]Begin##", curStep, flowUnit.getAtomTranCode());
        LOGGER.info("AtomTran note[{}]", flowUnit.getNote());
        Long startTime = Timer.getStartTime();
        FeContext feContext = flowStrand.getFeContext();
        ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext,flowUnit);
        AtomResult condAtomResult = PbCommonAtomUtils.elValChk(feContext, flowUnit, scopeValUnit);
        boolean condResultFlag = condAtomResult.isSuccess();
        LOGGER.info("IF条件判定结果:[{}]", condResultFlag);
        AstList list;
        if (condResultFlag) {
            list = ifModel.getIfBranch();
        } else {
            list = ifModel.getElseBranch();
        }
        atomResult.copyValue(execList(flowStrand, list));
        atomResult.setRetType(RetType.SUCCESS);
        flowStrand.setCurStep(ifModel.getEndStep());
        String endStep = flowStrand.getCurStep();
        LOGGER.info("##Step[{}] EndIF[{}]End!UseTime[{}]ms", endStep, flowUnit.getAtomTranCode(), Timer.getUsedTime(startTime));
    }

}
