/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.n.fbsp.atom.platform.seed.script;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.ShellScriptUtils;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * shell脚本执行
 * PATH|脚本路径
 * CONTENT|脚本内容
 * @author JiangTaiSheng
 */
@Atom("base.ShellScript")
public class ShellScriptImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellScriptImpl.class);

    /**
     *
     * @param tranContext 流程上下文对象
     * @param scopeValUnit 当前作用域
     * @param flowUnit 当前单元
     * @return Result原子交易结果
     *
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        String atomTranParam = flowUnit.getAtomTranParam();
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (atomTranParams.length != PARAM_NUM_TWO) {
            atomResult.setRetType(RetType.FAILED);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam));
        }
        String type = StrUtils.trim(atomTranParams[0]).toUpperCase();
        String script = atomTranParams[1];
        ShellScriptUtils.ScriptResult result;
        if(Objects.equals("PATH",type)){
            try {
                result = ShellScriptUtils.executeScriptByPath(script);
            } catch (Exception e) {
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("脚本运行异常"),e);
            }
        }else if(Objects.equals("CONTENT",type)){
            try {
                result = ShellScriptUtils.executeScriptByContent(script);
            } catch (Exception e) {
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("脚本运行异常"),e);
            }
        }else {
            atomResult.setRetType(RetType.FAILED);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未知操作类型,请检查参数配置[{}]", type));
        }
        // 处理执行结果
        if (result.getExitCode() != 0) {
            LOGGER.warn("Script execution failed with exit code[{}]", result.getExitCode());
            LOGGER.warn("Script output[{}]", result.getOutput());
            atomResult.setRetType(RetType.FAILED);
        } else {
            atomResult.setRetType(RetType.SUCCESS);
        }
        return atomResult;
    }
}
