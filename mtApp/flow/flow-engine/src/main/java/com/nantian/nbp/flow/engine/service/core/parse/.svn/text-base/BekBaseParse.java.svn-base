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
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bek语法处理，用于While块中停止循环
 * @author Administrator
 */
public class BekBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(BekBaseParse.class);

    @Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		AtomResult atomResult = new AtomResult();
		if(ASTType.BEK.equals(model.getType())){
			normalHandle(flowStrand,model);
		}else {
			throwAstTypeException(ASTType.BEK.getType(),model.getType());
		}
		return atomResult;
	}

	private void normalHandle(FlowStrand flowStrand,AstModel model){
		FlowUnit unit = model.getUnit();
		String curStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] Execute BEK[{}]Begin##",curStep,
				unit.getAtomTranCode());
		LOGGER.info("AtomTran note[{}]",unit.getNote());
		Long startTime = Timer.getStartTime();
		// 是否处在WHILE语法块内
		if(flowStrand.containsAstBlock(ASTType.WHILE)){
			flowStrand.setBreak(Boolean.TRUE);
		}else {
			LOGGER.warn("Bek needs to be within a While block, ignoring the current processing.");
		}
		LOGGER.info("##Step[{}] End BEK[{}]End!UseTime[{}]ms",curStep,
				unit.getAtomTranCode(),Timer.getUsedTime(startTime));
	}

}
