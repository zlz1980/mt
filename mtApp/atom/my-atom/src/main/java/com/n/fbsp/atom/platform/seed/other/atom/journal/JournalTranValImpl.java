package com.n.fbsp.atom.platform.seed.other.atom.journal;


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
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.EXT_INFO;
import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.TAG_SET;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE_P;
import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 将增强处理参数写入tran作用域下指定的流水对象中不涉及数据库操作
 * 1.原子交易参数：
 * tran作用域下指定的流水对象名称
 * 2.增强处理参数（以 | 分割）：
 * key|value
 * traceId|${req.trace}
 * reqId|${req.id}
 * 3.错误码及错误描述：
 * 无需配置
 *
 * @author JiangTaiSheng
 */
@Atom("fbsp.JournalTranVal")
public class JournalTranValImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalTranValImpl.class);

    /**
     * 变量赋值操作
     * ${req.id}|${val.id}.toUpperCase()
     * ${req.name}|'aaa'.toUpperCase()
     * *${req.name}|W001
     * <p>
     * 重写doService方法，用于处理特定的业务逻辑
     *
     * @param tranContext  交易上下文，包含交易流程的全局信息
     * @param scopeValUnit 作用域值单元，用于存储和传递作用域内的变量
     * @param flowUnit     流程单元，包含当前流程的具体信息和参数
     * @return 返回处理结果AtomResult对象，包含处理的状态和可能的返回值
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化执行结果对象
        AtomResult atomResult = new AtomResult();
        // 检查原子交易参数是否为空或未配置
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取tran内部作用域标签
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // tran内部param作用域转换为map对象
        PbScope<Object> journalScope = (PbScope<Object>) tranContext.getTranScope().get(atomTranParam);
        // 检查流水作用域是否存在
        if (ObjectUtils.isEmpty(journalScope)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域不存在,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域不存在,请检查参数配置[{}]", atomTranParam), null);
        }
        try {
            // 初始化原始哈希集合和扩展信息映射
            Set<String> oriHashSet = new HashSet<>();
            Map<String, Object> oriExtInfo = new HashMap<>();
            // 如果journalScope中已有TAG_SET或EXT_INFO，则分别赋值给oriHashSet和oriExtInfo
            if (!ObjectUtils.isEmpty(journalScope.get(TAG_SET))) {
                oriHashSet = (Set<String>) journalScope.get(TAG_SET);
            }
            if (!ObjectUtils.isEmpty(journalScope.get(EXT_INFO))) {
                oriExtInfo = (Map<String, Object>) journalScope.get(EXT_INFO);
            }
            ScopeValUnit reqScopeValUnit = new ScopeValUnit();
            // 设置流程参数值
            FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit);
            // 处理标签集合和扩展信息
            Set<String> newHashSet = JournalUtils.processTagSet(reqScopeValUnit);
            Map<String, Object> newExtInfo = JournalUtils.processExtInfo(reqScopeValUnit);
            // 更新journalScope中的数据
            journalScope.putAll(reqScopeValUnit);
            oriHashSet.addAll(newHashSet);
            journalScope.put(TAG_SET, oriHashSet);
            oriExtInfo.putAll(newExtInfo);
            journalScope.put(EXT_INFO, oriExtInfo);
            // 设置操作类型
            journalScope.put(OPERATE_TYPE, OPERATE_TYPE_P);
            // 更新交易上下文中的对应作用域
            MapUtils.setVal(tranContext.getTranScope(), atomTranParam, journalScope);
            // 设置执行结果为成功
            atomResult.setRetType(RetType.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域信息更新异常,异常信息[{}]", e.getMessage(), e);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域信息更新异常,异常信息[{}]", e.getMessage()), null);
        }
        // 日志输出当前流水作用域信息
        LOGGER.debug("当前流水作用域 [{}],内容 [{}]", "tran." + atomTranParam, journalScope);
        // 返回执行结果
        return atomResult;
    }

}
