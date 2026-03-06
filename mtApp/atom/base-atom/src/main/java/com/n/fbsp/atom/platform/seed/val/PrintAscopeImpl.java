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
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_DOT_SPILT;
import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_END_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.VAR_START_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

@Atom("base.PrintAscope")
public class PrintAscopeImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintAscopeImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        try {
            if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || Objects.equals(INIT_RET_VAL,
                    flowUnit.getAtomTranParam())) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg(
                        "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
            }
            // 获取作用域
            String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
            Object val;
            if (atomTranParam.contains(PARAM_DOT_SPILT)) {
                val = tranContext.getValue(VAR_START_FLAG + atomTranParam + VAR_END_FLAG);
            } else {
                val = tranContext.getScopeVal(atomTranParam);
            }
            String printStr = JsonUtil.writeValueAsString(val);
            LOGGER.info("目标打印变量:[{}], 打印内容:[{}]", atomTranParam, printStr);
        }catch (Exception e) {
            LOGGER.error("PrintAscope执行异常", e);
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}
