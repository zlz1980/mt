package com.n.fbsp.atom.platform.seed.other.atom.journal;


import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.n.fbsp.atom.platform.seed.other.service.IJournalService;
import com.n.fbsp.atom.platform.seed.other.utils.JournalUtils;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.BUSI_STATE;
import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.TRAN_STATE;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 流水状态更新
 *
 * @Author : 
 * @create 2024/11/15 09:18
 */
@Atom("fbsp.JournalStateUpdate")
public class JournalStateUpdateImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalStateUpdateImpl.class);

    @Autowired
    private IJournalService iJournalService;

    /**
     * 变量赋值操作
     * journalName|tranState|busiState
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
        // 初始化执行结果对象
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        //获取tran内部作用域标签
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 分割参数并处理参数列表和查询条件的映射关系
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (atomTranParams.length != PARAM_NUM_THREE) {
            // 记录错误日志
            LOGGER.error("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            // 抛出异常，指示原子交易参数数量错误
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }
        PbScope<Object> sysScope = tranContext.getSysScope();
        PbScope<Object> tranScope = tranContext.getTranScope();
        String journalBean = MapUtils.spiltVal(atomTranParams[0]);
        //tran内部param作用域转换为map对象
        PbScope<Object> journalScope = (PbScope<Object>) tranScope.get(journalBean);
        if (ObjectUtils.isEmpty(journalScope)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域不存在,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域不存在,请检查参数配置[{}]", atomTranParams[0]), null);
        }
        String tranState;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParams[1]))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到交易状态,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到交易状态,请检查参数配置[{}]", atomTranParams[1]), null);
        } else {
            tranState = (String) tranContext.getCtxVal(atomTranParams[1]).getVal();
            if (ObjectUtils.isEmpty(tranState)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "交易状态为空,请检查参数配置[{}]", atomTranParams[1]);
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("交易状态为空,请检查参数配置[{}]", atomTranParams[1]), null);
            }
        }
        String busiState;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParams[2]))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到业务状态,请检查参数配置[{}]", atomTranParams[2]);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到业务状态,请检查参数配置[{}]", atomTranParams[2]), null);
        } else {
            busiState = (String) tranContext.getCtxVal(atomTranParams[2]).getVal();
            if (ObjectUtils.isEmpty(busiState)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "业务状态为空,请检查参数配置[{}]", atomTranParams[2]);
                throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("业务状态为空,请检查参数配置[{}]", atomTranParams[2]), null);
            }
        }

        // 初始化操作计数器
        int count = -1;
        try {
            journalScope.put(TRAN_STATE, tranState);
            journalScope.put(BUSI_STATE, busiState);
            TEbJournalPo tEbJournalPo = JournalUtils.buildJournalPo(journalScope, sysScope);
            count = iJournalService.updateJournalState(tEbJournalPo);
            // 设置执行结果为成功
            atomResult.setRetType(RetType.SUCCESS);
            LOGGER.debug("tranState更新为 [{}],busiState更新为 [{}]", tranState, busiState);
            LOGGER.debug("当前流水作用域 [{}],内容 [{}]", "tran." + journalBean, journalScope);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水状态更新异常,异常信息[{}]", e.getMessage(), e);
            atomResult.setRetType(RetType.FAILED);
            return atomResult;
        } finally {
            LOGGER.info("流水状态更新完成,size[{}]", count);
            // 将操作计数器存入作用域
            scopeValUnit.put(RESULT_COUNT, count);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }
}
