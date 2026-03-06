/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.WhileModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.ERGODIC_IDX;
import static com.nantian.nbp.flow.engine.service.api.Constants.WHILE_PARAM_SPILT_FLAG;

/**
 * While语法处理
 * @author Administrator
 */
public class WhileBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(WhileBaseParse.class);

	@Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		AtomResult atomResult = new AtomResult();
		if (model instanceof WhileModel) {
			WhileModel whileModel = (WhileModel) model;
			atomResult = normalHandle(flowStrand,whileModel, atomResult);
		}else {
			throwAstTypeException("WhileModel",model.getType());
		}
		return atomResult;
	}

	/**
	 * WHILE语句接受的参数格式  ${item}:${srcData.list} 或者 单个bool型参数
	 * */
	private AtomResult normalHandle(FlowStrand flowStrand, WhileModel whileModel, AtomResult atomResult){
		FlowUnit flowUnit = whileModel.getUnit();
		ASTType whileType = whileModel.getType();
		flowStrand.addAstBlock(whileType);
		try {
			String curStep = flowStrand.getCurStep();
			LOGGER.info("##Step[{}] Execute WhileModel[{}]Begin##",curStep,
					flowUnit.getAtomTranCode());
			LOGGER.info("AtomTran note[{}]",flowUnit.getNote());
			Long startTime = Timer.getStartTime();
			FeContext feContext = flowStrand.getFeContext();
			String paramStr = flowUnit.getAtomTranParam();

			boolean ergodicFlag = paramStr.contains(WHILE_PARAM_SPILT_FLAG);
			// 对List场景遍历时，清理循环索引
			if(ergodicFlag){
				cleanErgodicIdx(feContext,flowUnit);
			}

			ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext,flowUnit);

			boolean condResultFlag = condResult(feContext,flowUnit,scopeValUnit,ergodicFlag);
			AstList loopBody = whileModel.getLoop();
			while (condResultFlag) {
				LOGGER.debug("ConditionModel returnFlag is [{}]",condResultFlag);
				atomResult = execList(flowStrand,loopBody);
				if(flowStrand.isBreak()){
					break;
				}
				if(flowStrand.isReturn()||flowStrand.isExit()){
					return atomResult;
				}
				condResultFlag = condResult(feContext,flowUnit,scopeValUnit,ergodicFlag);
			}
			if(flowStrand.isBreak()){
				flowStrand.setBreak(Boolean.FALSE);
			}
			flowStrand.setCurStep(whileModel.getEndStep());
			String endStep = flowStrand.getCurStep();
			LOGGER.info("##Step[{}] EndWhileModel[{}]End!UseTime[{}]ms",endStep,
					flowUnit.getAtomTranCode(),Timer.getUsedTime(startTime));
			return atomResult;
		}finally {
			flowStrand.removeAstBlock(whileType);
		}
	}

	private boolean condResult(FeContext feContext, FlowUnit flowUnit, ScopeValUnit scopeValUnit, boolean ergodicFlag){
		if(ergodicFlag){
			return PbCommonAtomUtils.pbValWhile(feContext,flowUnit,scopeValUnit).isSuccess();
		}else {
			return PbCommonAtomUtils.elValChk(feContext,flowUnit,scopeValUnit).isSuccess();
		}
	}

	private void cleanErgodicIdx(FeContext feContext,FlowUnit flowUnit){
		PbScope<Object> ctxMap = feContext.getProps();
		String scope = flowUnit.getAscope();
		String idxKey = ERGODIC_IDX+scope;
		Object scopeUnitObj = ctxMap.get(scope);
		if(Objects.nonNull(scopeUnitObj) && scopeUnitObj instanceof ScopeValUnit){
			ScopeValUnit scopeValUnit = (ScopeValUnit)scopeUnitObj;
			scopeValUnit.remove(idxKey);
		}
	}
}
