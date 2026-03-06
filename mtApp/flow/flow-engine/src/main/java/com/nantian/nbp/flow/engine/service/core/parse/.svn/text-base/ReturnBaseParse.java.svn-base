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
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.flow.engine.service.api.util.PbCommonAtomUtils;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;

/**
 * Return语法处理
 * @author Administrator
 */
public class ReturnBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnBaseParse.class);

	private static final String INIT_RET_VAL = "00000";
	private static final String RET_VAL = "retVal";

	@Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		AtomResult atomResult = new AtomResult();
		if(ASTType.RETURN.equals(model.getType())){
			normalHandle(flowStrand,model, atomResult);
		}else {
			throwAstTypeException("RETURN",model.getType());
		}
		return atomResult;
	}

	private void normalHandle(FlowStrand flowStrand,AstModel model, AtomResult atomResult){
		FlowUnit unit = model.getUnit();
		String curStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] Execute return[{}]Begin##",curStep,
				unit.getAtomTranCode());
		LOGGER.info("AtomTran note[{}]",unit.getNote());
		Long startTime = Timer.getStartTime();
		flowStrand.setReturn(Boolean.TRUE);
		FeContext feContext = flowStrand.getFeContext();
		String retVal = unit.getAtomTranParam();
		ScopeValUnit scopeValUnit = PbCommonAtomUtils.createPutCtxScopeValUnit(feContext,unit);
		//多参赋值
		if(!StringUtils.hasText(retVal) || INIT_RET_VAL.equals(retVal)){
			FlowParaUtils.setFlowParaValues(feContext, scopeValUnit, unit);
		}else {//单参赋值
			String keyName = RET_VAL;
			String[] params = StringUtils.delimitedListToStringArray(retVal, PARAM_SPILT_FLAG);
			if(params.length > PARAM_NUM_TWO) {
				throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(), "参数赋值格式非法，最多仅允许一个|分割,请检查参数配置["+retVal+"]");
			}
			if(params.length == PARAM_NUM_TWO){
				keyName = params[0];
				retVal = params[1];
			}
			FlowParaUtils.setFlowParaValue(feContext, scopeValUnit,keyName,retVal);
		}
		atomResult.setRetType(RetType.SUCCESS);
		feContext.setOutScope(scopeValUnit);
		flowStrand.setRetType(atomResult.getRetType());
		PbCommonAtomUtils.atomResultAfterHandle(scopeValUnit,atomResult);
		if(!flowStrand.isBreak() &&
				FlowStrand.MAIN_PROCESS.equals(flowStrand.getFlowType())){
			flowStrand.setExit(true);
			LOGGER.info("MainProcess return -> Exit..");
		}
		LOGGER.info("ReturnValue is [{}]",retVal);
		LOGGER.info("##Step[{}] EndReturn[{}]End!UseTime[{}]ms",curStep,
				unit.getAtomTranCode(),Timer.getUsedTime(startTime));
	}

}
