/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.core;

import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.core.parse.BaseParseStrategy;
import com.nantian.nbp.flow.engine.service.core.parse.MainFlowBaseParse;
import com.nantian.nbp.flowengine.model.FlowContainer;

/**
 * 流程引擎的控制器，负责一些控制流的语义识别 (flowengine-step-02)
 * @author pengw at Jul 28, 2020 2:09:51 PM
 * @version V5.0
 */
public class FlowDispatch {
	private static final BaseParseStrategy PARSE_STRATEGY = new MainFlowBaseParse();

	public static void handleFlow(FlowContainer flowContainer, FeContext feContext){
		FlowStrand flowStrand = new FlowStrand(feContext);
		PARSE_STRATEGY.execList(flowStrand,flowContainer.getExecList());
	}

}
