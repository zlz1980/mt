package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flowengine.model.AstModel;

import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * @author Administrator
 */
public class MainFlowBaseParse extends BaseParseStrategy{

    @Override
    @Deprecated
    public AtomResult doHandle(FlowStrand flowStrand, AstModel model) {
        throw new FlowException(T_X0005091.getCode(),T_X0005091.getCodeMsg("此方法不允许被调用"));
    }
}
