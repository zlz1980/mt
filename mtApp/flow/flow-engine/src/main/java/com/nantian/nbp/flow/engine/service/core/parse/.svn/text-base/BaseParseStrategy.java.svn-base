package com.nantian.nbp.flow.engine.service.core.parse;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FlowStrand;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.BizException;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.exception.PbRunTimeException;
import com.nantian.nbp.flow.engine.service.api.exception.ThrowException;
import com.nantian.nbp.flowengine.model.ASTType;
import com.nantian.nbp.flowengine.model.AstList;
import com.nantian.nbp.flowengine.model.AstModel;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 流程引擎通用解析器，负责解析和调度原子组件
 *
 */
@Component
public abstract class BaseParseStrategy {

    protected static final String ZERO_STR = "0";
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseParseStrategy.class);

    /**
     * 不同类型节点处理逻辑
     *
     * @param flowStrand 流程状态
     * @param model      语法节点
     * @return Result结果
     */
    public abstract AtomResult doHandle(FlowStrand flowStrand, AstModel model);

    /**
     * 单步执行原子组件
     *
     * @param model 语法节点
     * @return Result结果
     */
    public AtomResult execModel(FlowStrand flowStrand, AstModel model) {
        AtomResult atomResult = new AtomResult();
        if (flowStrand.isExit()) {
            return atomResult;
        }
        ASTType type = model.getType();
        FlowUnit unit = model.getUnit();
        flowStrand.setCurStep(unit.getAtomTranNo());
        flowStrand.addStep(flowStrand.getCurStep());
        // 错误码支持变量
        String errCode = (String) flowStrand.getFeContext().getValue(StrUtils.toStr(unit.getErrCode(), ZERO_STR));
        BaseParseStrategy parse = ParseStrategyFactory.creator(type, flowStrand.getFeTranCode());
        try {
            atomResult = parse.doHandle(flowStrand, model);
        } catch (PbRunTimeException e) {
            flowStrand.setCurStep(unit.getAtomTranNo());
            e.setErrStep(flowStrand.getCurStep());
            throw e;
        } catch (Exception e) {
            flowStrand.setCurStep(unit.getAtomTranNo());
            exceptionResult(atomResult, e);
            if (!errCode.equals(ZERO_STR)) {
                throw new PbRunTimeException(errCode, atomResult.getErrMsg(), flowStrand.getCurStep(), e);
            } else {
                throw new FlowException(atomResult.getErrCode(), flowStrand.getFeTranCode(), atomResult.getErrMsg(),
                        flowStrand.getCurStep(), unit.getAtomTranCode(), e);
            }
        }
        return atomResult;
    }

    public AtomResult execList(FlowStrand flowStrand, AstList list) {
        AtomResult atomResult = new AtomResult();
        if (isEmptyList(list)) {
            return atomResult;
        }
        if (flowStrand.isExit()) {
            return atomResult;
        }
        return execListFlow(flowStrand, list, atomResult);
    }

    protected AtomResult execListFlow(FlowStrand flowStrand, List<AstModel> list, AtomResult atomResult) {
        for (AstModel model : list) {
            AtomResult res = execModel(flowStrand, model);
            boolean isBreak = flowStrand.isBreak() || flowStrand.isExit();
            if (isBreak) {
                break;
            }
            if (flowStrand.isReturn()) {
                LOGGER.debug("ExecListFlow return..");
                return res;
            }
        }
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    private boolean isEmptyList(List<AstModel> list) {
        return list == null || list.isEmpty();
    }

    private void exceptionResult(AtomResult atomResult, Exception e) {
        atomResult.setRetType(RetType.UNKNOWN);
        String errCode = null;
        if (e instanceof BizException) {
            atomResult.setRetType(RetType.FAILED);
            errCode = ((BizException) e).getErrCode();
        } else if (e instanceof ThrowException) {
            errCode = ((ThrowException) e).getErrCode();
        }
        atomResult.setErrCode(errCode);
        atomResult.setErrMsg(e.getMessage());
    }

    protected void throwAstTypeException(String modeName, ASTType astType) {
        throw new FlowException(String.format("Must be %s,[%s]is not support", modeName, astType));
    }
}
