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
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit语法处理
 * @author Administrator
 */
public class ExitBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExitBaseParse.class);
	@Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		AtomResult atomResult = new AtomResult();
		if(ASTType.EXIT.equals(model.getType())){
			normalHandle(flowStrand,model);
			atomResult.setRetType(RetType.SUCCESS);
		}else {
			throwAstTypeException(ASTType.EXIT.getType(),model.getType());
		}
		return atomResult;
	}

	private void normalHandle(FlowStrand flowStrand,AstModel model){
		FlowUnit flowUnit = model.getUnit();
		String curStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] Execute Exit[{}]Begin##",curStep,flowUnit.getAtomTranCode());
		flowStrand.setExit(true);
		LOGGER.info("##Step[{}] EndExit[{}]End!",curStep,flowUnit.getAtomTranCode());
	}
}
