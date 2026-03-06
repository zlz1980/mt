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


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE_U;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Atom("fbsp.JournalUpdate")
public class JournalUpdateImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalUpdateImpl.class);

    private final JournalTranValImpl journalTranVal;

    @Autowired
    private IJournalService journalService;

    public JournalUpdateImpl(JournalTranValImpl journalTranVal) {
        this.journalTranVal = journalTranVal;
    }

    /**
     * 原子交易主要逻辑实现
     *
     * @param tranContext
     * @param scopeValUnit 本单元上下文对象
     * @param flowUnit     流程单元，包括配置参数信息
     * @return Result结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化原子交易结果对象
        AtomResult atomResult = new AtomResult();

        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取tran内部作用域标签
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 获取当前流水作用域
        PbScope<Object> journalScope = (PbScope<Object>) tranContext.getTranScope().get(atomTranParam);
        if (ObjectUtils.isEmpty(journalScope)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域不存在,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域不存在,请检查参数配置[{}]", atomTranParam), null);
        }
        // 处理update追加参数
        AtomResult updateAtomResult = journalTranVal.doService(tranContext, scopeValUnit, flowUnit);
        // 如果更新操作成功
        if (updateAtomResult.isSuccess()) {
            // 初始化操作计数器
            int count = -1;
            // 将作用域转换为数据库记录对象
            TEbJournalPo tEbJournalPo = JournalUtils.buildJournalPo(journalScope, tranContext.getSysScope());
            // 更新流水表内容
            try {
                // 执行更新操作
                count = journalService.updateJournalInfo(tEbJournalPo);
                JournalUtils.buildJournalScope(tEbJournalPo, journalScope, OPERATE_TYPE_U);
                MapUtils.setVal(tranContext.getTranScope(), atomTranParam, journalScope);
                atomResult.setRetType(RetType.SUCCESS);
                LOGGER.debug("当前流水作用域 [{}],内容 [{}]", "tran." + atomTranParam, journalScope);
            } catch (Exception e) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水信息更新异常,异常信息 [{}]", e.getMessage(), e);
                // 设置事务结果为失败，并返回结果对象
                atomResult.setRetType(RetType.FAILED);
                LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
                return atomResult;
            } finally {
                LOGGER.info("流水更新完成,size[{}]", count);
                // 将操作计数器存入作用域
                scopeValUnit.put(RESULT_COUNT, count);
            }
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }
}
