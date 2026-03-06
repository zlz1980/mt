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
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.ProcMode;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proc语法处理
 * @author Administrator
 */
public class ProcBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcBaseParse.class);

	@Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		if (model instanceof ProcMode) {
			ProcMode procMode = (ProcMode) model;
			return normalHandle(flowStrand,procMode);
		}else {
			throwAstTypeException("ProcMode",model.getType());
		}
		return new AtomResult(RetType.UNKNOWN);
	}

	private AtomResult normalHandle(FlowStrand flowStrand, ProcMode procMode){
		FlowUnit flowUnit = procMode.getUnit();
		String curStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] Execute ProcModel[{}]Begin##",curStep,
				flowUnit.getAtomTranCode());
		LOGGER.info("AtomTran note[{}]",flowUnit.getNote());
		Long startTime = Timer.getStartTime();
		AtomResult atomResult = execList(flowStrand,procMode.getProcList());
		flowStrand.setCurStep(procMode.getEndStep());
		String endStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] EndProcModel[{}]End!UseTime[{}]ms",endStep,
				flowUnit.getAtomTranCode(),Timer.getUsedTime(startTime));
		return atomResult;
	}

}
