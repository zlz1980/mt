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
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.ZERO_PARAM;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 变量断言，对传入的原子交易参数进行断言判定，符合条件则抛出当前步骤配置的错误码及错误描述
 *
 * @author JiangTaiSheng
 */
@Atom("base.PbAssert")
public class PbAssertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbAssertImpl.class);

    /**
     * 变量判断操作
     * <p>
     * 原子交易参数：
     * JAVA常规判断语句组合，要求结果为布尔型，支持==  !=  ||  &&  >  <等运算符号
     * ${req.id} > 1 && ${req.age} < 10
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
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        if (ObjectUtils.isEmpty(flowUnit.getErrCode()) || ZERO_PARAM.equals(flowUnit.getErrCode())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode()));
        }
        if (ObjectUtils.isEmpty(flowUnit.getErrorInfo()) || ZERO_PARAM.equals(flowUnit.getErrCode())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo()));
        }
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String errCode = (String) tranContext.getValue(StrUtils.trim(flowUnit.getErrCode()));
        String errorInfo = (String) tranContext.getValue(StrUtils.trim(flowUnit.getErrorInfo()));
        Boolean val = tranContext.getCtxBoolVal(atomTranParam);
        LOGGER.info("变量断言结果[{}]", val);
        if (Objects.equals(Boolean.FALSE, val)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("变量断言表达式[{}]", tranContext.printElVals(atomTranParam));
            }
            throw new FlowException(errCode, errorInfo);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}
