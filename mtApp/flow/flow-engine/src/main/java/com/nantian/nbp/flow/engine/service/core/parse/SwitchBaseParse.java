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
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.flowengine.model.CaseModel;
import com.nantian.nbp.flowengine.model.SwitchModel;
import com.nantian.nbp.utils.StrUtils;
import com.nantian.nbp.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Switch语法处理
 * @author Administrator
 */
public class SwitchBaseParse extends BaseParseStrategy {
	private static final Logger LOGGER = LoggerFactory.getLogger(SwitchBaseParse.class);
	@Override
	public AtomResult doHandle(FlowStrand flowStrand,AstModel model) {
		AtomResult atomResult = new AtomResult();
		if (model instanceof SwitchModel) {
			SwitchModel switchModel = (SwitchModel) model;
			LinkedHashMap<String,CaseModel> caseList = switchModel.getCaseBranch();

			normalHandle(flowStrand,switchModel, caseList, atomResult);
		}else {
			throwAstTypeException("SwitchModel",model.getType());
		}
		return atomResult;
	}

	private void normalHandle(FlowStrand flowStrand,SwitchModel switchModel,
							  LinkedHashMap<String,CaseModel> caseList, AtomResult atomResult){
		FlowUnit flowUnit = switchModel.getUnit();
		String curStep = flowStrand.getCurStep();
		LOGGER.info("##Step[{}] Execute SwitchModel[{}]Begin##",curStep,
				flowUnit.getAtomTranCode());
		LOGGER.info("AtomTran note[{}]",flowUnit.getNote());
		Long startTime = Timer.getStartTime();

		AtomResult conditionAtomResult = condRes(flowStrand.getFeContext(),flowUnit);
		String caseValue = null;
		if (conditionAtomResult.isSuccess()) {
			caseValue = conditionAtomResult.getMsg();
		}
		// 是否走默认分支标识
		boolean flag = false;
		AstList list;
		if (caseValue != null) {
			for (CaseModel caseModel : caseList.values()) {
				if (caseValue.equals(caseModel.getValue())) {
					flag = true;
					list = caseModel.getCaseList();
					atomResult.copyValue(execList(flowStrand,list));
					if (caseModel.isBreak()) {
						break;
					}
				} else if (flag) {
					list = caseModel.getCaseList();
					atomResult.copyValue(execList(flowStrand,list));
					if (caseModel.isBreak()) {
						break;
					}
				}
			}
			if (!flag) {
				defaultExec(flowStrand,caseList, atomResult);
			}
		}else {
			defaultExec(flowStrand,caseList, atomResult);
		}
		flowStrand.setCurStep(switchModel.getEndStep());
		String endStep = flowStrand.getCurStep();
		long time = Timer.getUsedTime(startTime);
		LOGGER.info("##Step[{}] EndSwitchModel[{}]End!UseTime[{}]ms",endStep,
				flowUnit.getAtomTranCode(),time);
	}

	private void defaultExec(FlowStrand flowStrand,LinkedHashMap<String,CaseModel> caseList, AtomResult atomResult) {
		CaseModel caseModel = caseList.get(CaseModel.DEFAULT);
		if(Objects.nonNull(caseModel)){
			AstList list = caseModel.getCaseList();
			atomResult.copyValue(execList(flowStrand,list));
		}
	}

	private AtomResult condRes(FeContext feContext,FlowUnit flowUnit) {
		AtomResult atomResult = new AtomResult(RetType.FAILED);
		String param = flowUnit.getAtomTranParam();
		if(Objects.isNull(param)){
			return atomResult;
		}
		String val = StrUtils.toStrDefNull(feContext.getValue(param));
		atomResult.setMsg(val);
		LOGGER.debug("switch val[{}]",val);
		atomResult.setRetType(RetType.SUCCESS);
		return atomResult;
	}
}
