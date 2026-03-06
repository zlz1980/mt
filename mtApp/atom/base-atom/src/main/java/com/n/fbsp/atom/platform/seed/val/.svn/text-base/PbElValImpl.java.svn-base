/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 变量赋值，对传入的原子交易参数进行赋值操作，并将赋值后的结果写入到当前作用域对象中
 *
 * @author JiangTaiSheng
 */
@Atom("base.PbElVal")
public class PbElValImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbElValImpl.class);

    /**
     * 变量赋值操作
     * ${req.id}|${val.id}.toUpperCase()
     * ${req.name}|'aaa'.toUpperCase()
     * *${req.name}|W001
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象
     * @param flowUnit     当前单元
     * @return Result
     *
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        // 日志输出增强处理参数信息
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("增强处理参数 [{}]", JsonUtil.objToString(flowUnit.getParamList()));
        }
        FlowParaUtils.setFlowParaValues(tranContext, scopeValUnit, flowUnit);
        atomResult.setRetType(RetType.SUCCESS);

        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }
}
