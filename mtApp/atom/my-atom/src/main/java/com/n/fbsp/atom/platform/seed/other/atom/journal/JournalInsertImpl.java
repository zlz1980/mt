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
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Set;


import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.TAG_SET;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE_I;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 实现主流水的新增，数据处理完成后直接写入T_EB_JOURNAL同步写入tran.XXXJournal作用域对象
 * 1.原子交易参数：
 * 流水作用域名称
 * defJournal
 * 2.增强处理参数（以 | 分割）：
 * key|value
 * traceId|${req.trace}
 * reqId|${req.id}
 * 3.错误码及错误描述：
 * 无需配置
 *
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Atom("fbsp.JournalInsert")
public class JournalInsertImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalInsertImpl.class);

    @Autowired
    private IJournalService journalService;

    /**
     * 重写doService方法，用于处理特定的业务逻辑
     *
     * @param tranContext  交易上下文，包含交易流程的全局信息
     * @param scopeValUnit 作用域值单元，用于存储和传递作用域内的变量
     * @param flowUnit     流程单元，包含当前流程的具体信息和参数
     * @return 返回处理结果AtomResult对象，包含处理的状态和可能的返回值
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        //初始化执行结果对象，并默认设置为成功
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        //获取流程单元中的参数，并做初始化判断
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        //判断是否为保留字，保留字则报错不允许使用
        if (TRAN_SCOPE_RESERVED_KEYS.contains(atomTranParam)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域名称为保留字[{}],请检查参数配置[{}]", atomTranParam, TRAN_SCOPE_RESERVED_KEYS);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域名称为保留字[{}],请检查参数配置[{}]", atomTranParam, TRAN_SCOPE_RESERVED_KEYS), null);
        }
        PbScope<Object> sysScope = tranContext.getSysScope();
        PbScope<Object> tranScope = tranContext.getTranScope();
        PbScope<Object> journalScope = (PbScope<Object>) tranScope.get(atomTranParam);
        if (!ObjectUtils.isEmpty(journalScope)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域已存在,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域已存在,请检查参数配置[{}]", atomTranParam), null);
        } else {
            journalScope = new PbScope<>();
        }
        //初始化本地变量count，用于后续的操作计数
        int count;
        try {
            ScopeValUnit reqScopeValUnit = new ScopeValUnit();
            FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit);
            Set<String> hashSet = JournalUtils.processTagSet(reqScopeValUnit);
            journalScope.putAll(reqScopeValUnit);
            journalScope.put(TAG_SET, hashSet);
            //PO 数据映射
            TEbJournalPo tEbJournalPo = JournalUtils.buildJournalPo(journalScope, sysScope);
            //必输项检查
            JournalUtils.validateFields(tEbJournalPo, flowUnit.getTranCode());
            LOGGER.info("正在写入流水,平台流水号[{}],请求方业务主键:[{}]", tEbJournalPo.getJourNo(), tEbJournalPo.getBusiKey());
            //插入流水表
            count = journalService.insertJournalInfo(tEbJournalPo);
            //更新tran作用域中的值对象
            if (count >= 1) {
                JournalUtils.buildJournalScope(tEbJournalPo, journalScope, OPERATE_TYPE_I);
                //流水写入tranScope
                MapUtils.setVal(tranScope, atomTranParam, journalScope);
                LOGGER.debug("当前流水作用域 [{}],内容 [{}]", "tran." + atomTranParam, journalScope);
            }
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水信息插入异常,异常信息[{}]", e.getMessage(), e);
            count = -1;
            scopeValUnit.put(RESULT_COUNT, count);
            atomResult.setRetType(RetType.FAILED);
            atomResult.setErrMsg(T_X0005091.getCodeMsg(e.getMessage()));
            LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
            return atomResult;
        }
        LOGGER.info("流水新增完成,size[{}]", count);
        scopeValUnit.put(RESULT_COUNT, count);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        //返回执行结果对象
        return atomResult;
    }

}
