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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_ONE;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 判断给定目标值是否为NULL或EMPTY，支持原子交易参数和增强处理参数同时使用，执行时按照原子交易参数、
 * 增强处理参数顺序进行校验，任意参数为空原子交易整体返回失败。
 *
 * @author Lianghaizhen
 */
@Atom("base.ParamEmptyCheck")
public class ParamEmptyCheckImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamEmptyCheckImpl.class);

    /**
     * 1.原子交易参数：
     * 待判定参数
     * 2.增强处理参数：
     * 待判定参数
     * <p>
     * 应用举例：
     * 原子参数设置：
     * ${req.param1}
     * <p>
     * 增强处理参数设置：
     * ${req.param2}
     * ${req.param3}
     * ${req.param4}
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

        AtomResult atomResult = new AtomResult();
        StringBuilder errParam = new StringBuilder();
        if (!(ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam()))) {
            // 原子交易参数判断
            String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
            if (atomTranParam.contains(PARAM_SPILT_FLAG)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam));
            }
            Object val = tranContext.getCtxVal(atomTranParam).getVal();
            if (ObjectUtils.isEmpty(val)) {
                errParam.append("[").append(atomTranParam).append("]");
            }
        }
        // 增强处理参数判断
        List<String> paramList = flowUnit.getParamList();
        if (!CollectionUtils.isEmpty(paramList)) {
            // 日志输出增强处理参数信息
            LOGGER.debug("增强处理参数 [{}]", JsonUtil.objToString(paramList));
            for (String para : paramList) {
                if (para.contains(PARAM_SPILT_FLAG)) {
                    LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数个数不为1,请检查参数配置[{}]", para);
                    throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数个数不为1,请检查参数配置[{}]", para));
                }
                Object val = tranContext.getCtxVal(StrUtils.trim(para)).getVal();
                if (ObjectUtils.isEmpty(val)) {
                    errParam.append("[").append(para).append("]");
                }
            }
        }
        if (errParam.length() > 0) {
            LOGGER.debug("以下参数校验为空 [{}] ,请检查!", errParam);
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg(String.format("以下参数校验为空 [%s] ,请检查!", errParam));
        } else {
            atomResult.setRetType(RetType.SUCCESS);
        }
        return atomResult;
    }
}
