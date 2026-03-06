/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;

/**
 * 原子交易接口
 * @author Administrator
 */
public interface AtomService {

	/**
	 * 原子交易主要逻辑实现
	 *
	 * @param tranContext
	 * @param scopeValUnit 本单元上下文对象
	 * @param flowUnit     流程单元，包括配置参数信息
	 * @return Result结果
	 */
	AtomResult doService(TranContext tranContext, final ScopeValUnit scopeValUnit, final FlowUnit flowUnit);

}
