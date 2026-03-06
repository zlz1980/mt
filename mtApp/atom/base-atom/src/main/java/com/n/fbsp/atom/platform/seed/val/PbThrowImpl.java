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
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.exception.ThrowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.ZERO_PARAM;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 主动抛异常,对于配置的原子交易的错误码和错误描述进行校验，校验通过主动抛出异常信息。
 *
 * @author JiangTaiSheng
 */
@Atom("base.PbThrow")
public class PbThrowImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbThrowImpl.class);

    /**
     * 主动抛异常
     * 错误码及错误描述：
     * 错误码:${tran.errCode}
     * 错误描述:${setErrMsg.errMsg}
     * 原子交易判断错误码及错误描述是否配置，如果配置则抛出throw new ThrowException(errCode, errorInfo)
     *
     * @param tranContext
     * @param scopeValUnit 流程上下文对象
     * @param flowUnit     当前单元
     * @return AtomResult
     *
     * @throws FlowException 流程异常
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        if (ObjectUtils.isEmpty(flowUnit.getErrCode()) || Objects.equals(ZERO_PARAM, flowUnit.getErrCode())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode()));
        }
        if (ObjectUtils.isEmpty(flowUnit.getErrorInfo()) || Objects.equals(ZERO_PARAM, flowUnit.getErrorInfo())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo()));
        }
        String errCode = (String) tranContext.getValue(StrUtils.trim(flowUnit.getErrCode()));
        String errorInfo = (String) tranContext.getValue(StrUtils.trim(flowUnit.getErrorInfo()));
        LOGGER.info("主动抛出异常,错误码[{}],错误描述[{}]", errCode, errorInfo);
        throw new ThrowException(errCode, errorInfo);
    }
}
