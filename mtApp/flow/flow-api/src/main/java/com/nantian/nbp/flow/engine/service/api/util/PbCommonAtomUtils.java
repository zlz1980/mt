package com.nantian.nbp.flow.engine.service.api.util;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.FeContext;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.ERGODIC_IDX;
import static com.nantian.nbp.flow.engine.service.api.Constants.IGNORE_EXCEPTION;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.WHILE_PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.ZERO_PARAM;
import static com.nantian.nbp.flow.engine.service.api.context.AtomResult.ATOM_RESULT_KEY;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author Administrator
 */
public class PbCommonAtomUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbCommonAtomUtils.class);
    /**
     * 验证错误信息
     * 该方法用于检查给定的ScopeValUnit和FlowUnit中的错误信息是否已配置或为空
     * 如果错误码或错误描述为空或未配置，则设置结果类型为失败，并抛出FlowException异常
     *
     * @param flowUnit    流单元，包含错误码和错误描述等信息
     * @param result      流程结果对象，用于设置返回类型
     */
    public static void validateErrInfo(FlowUnit flowUnit, AtomResult result) {
        if(IGNORE_EXCEPTION.equals(flowUnit.getAbnProcType())) {
            return;
        }
        // 检查错误码是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getErrCode()) || ZERO_PARAM.equals(flowUnit.getErrCode())) {
            // 设置结果类型为失败
            result.setRetType(RetType.FAILED);
            // 记录错误日志
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode());
            // 抛出异常，指示错误码配置问题
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误码为空或未配置,请检查参数配置[{}]", flowUnit.getErrCode()));
        }
        // 检查错误描述是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getErrorInfo()) || ZERO_PARAM.equals(flowUnit.getErrorInfo())) {
            // 设置结果类型为失败
            result.setRetType(RetType.FAILED);
            // 记录错误日志
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo());
            // 抛出异常，指示错误描述配置问题
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易错误描述为空或未配置,请检查参数配置[{}]", flowUnit.getErrorInfo()));
        }
    }

    /**
     * 针对IF或者WHILE语句的单个bool型参数进行判定
     * */
    public static AtomResult elValChk(FeContext feContext, FlowUnit flowUnit, ScopeValUnit scopeValUnit) {
        AtomResult atomResult = new AtomResult();
        String param = flowUnit.getAtomTranParam();
        if(Objects.isNull(param)){
            throw new FlowException(FlowException.EFLW001,feContext.getFeTranCode(),
                    "param is null",null);
        }
        Boolean val = feContext.getCtxBoolVal(param);
        atomResult.setRetType(val);
        if(Objects.equals(Boolean.FALSE,val)){
            String errCode = flowUnit.getErrCode();
            atomResult.setErrCode(errCode);
        }
        atomResultAfterHandle(scopeValUnit,atomResult);
        return atomResult;
    }

    /**
     * 针对WHILE语句的参数类型格式  ${item}:${srcData.list} 进行判定，
     * 通过内置在ScopeValUnit中的ERGODIC_NEXT_IDX_?循环索引值识别是否循环结束
     * */
    public static AtomResult pbValWhile(FeContext feContext, FlowUnit flowUnit, ScopeValUnit scopeValUnit) {
        String paramStr = flowUnit.getAtomTranParam();
        String[] params = StringUtils.delimitedListToStringArray(paramStr, WHILE_PARAM_SPILT_FLAG);
        if(params.length != PARAM_NUM_TWO){
            throw new FlowException(ErrorCodeEnum.T_C0003092.getCode(),"参数格式不合法，需提供以:分割的参数形式");
        }
        AtomResult atomResult = new AtomResult();
        List<Map<String,Object>> srcList = (List<Map<String,Object>>) feContext.getCtxVal(params[1]).getVal();
        if(Objects.isNull(srcList) || srcList.isEmpty()){
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg("srcList is null or empty");
            atomResult.setErrCode(flowUnit.getErrCode());
            return atomResult;
        }

        String idxKey = ERGODIC_IDX+flowUnit.getAscope();
        Integer idx = (Integer) scopeValUnit.get(idxKey);
        if(Objects.isNull(idx)){
            idx = 0;
        }

        if(idx >= srcList.size()){
            scopeValUnit.remove(idxKey);
            atomResult.setRetType(RetType.FAILED);
            atomResultAfterHandle(scopeValUnit,atomResult);
            return atomResult;
        }

        scopeValUnit.put(MapUtils.spiltVal(params[0]),srcList.get(idx));
        idx = idx + 1;
        scopeValUnit.put(idxKey, idx);
        atomResult.setRetType(RetType.SUCCESS);
        atomResultAfterHandle(scopeValUnit,atomResult);
        return atomResult;
    }

    public static ScopeValUnit createPutCtxScopeValUnit(FeContext feContext, FlowUnit unit) {
        String scope = unit.getAscope();
        PbScope<Object> ctxMap = feContext.getProps();
        ScopeValUnit scopeValUnit = new ScopeValUnit();
        ctxMap.put(scope, scopeValUnit);
        return scopeValUnit;
    }

    public static void atomResultAfterHandle(ScopeValUnit scopeValUnit, AtomResult atomResult){
        scopeValUnit.put(ATOM_RESULT_KEY, atomResult.resToPbScope());
    }
}
