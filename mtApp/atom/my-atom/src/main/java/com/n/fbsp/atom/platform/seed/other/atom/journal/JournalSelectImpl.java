package com.n.fbsp.atom.platform.seed.other.atom.journal;

import com.google.common.collect.ImmutableSet;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;


import static com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo.*;
import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE_S;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 流水查询实现类，用于在流程中查询流水信息。
 *
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Atom("fbsp.JournalSelect")
public class JournalSelectImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalSelectImpl.class);

    @Autowired
    private IJournalService journalService;

    /**
     * 执行流水查询服务。
     *
     * @param tranContext  交易上下文，包含交易流程的全局信息
     * @param scopeValUnit 当前作用域值单元
     * @param flowUnit     当前流程单元
     * @return 流水查询结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化返回结果对象
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取tran内部作用域标签
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        //判断是否为保留字，保留字则报错不允许使用
        if (TRAN_SCOPE_RESERVED_KEYS.contains(atomTranParam)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域名称为保留字[{}],请检查参数配置[{}]", atomTranParam, TRAN_SCOPE_RESERVED_KEYS);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域名称为保留字[{}],请检查参数配置[{}]", atomTranParam, TRAN_SCOPE_RESERVED_KEYS), null);
        }
        // 定义计数变量，用于记录流水查询结果数量
        int count = -1;
        PbScope<Object> tranScope = tranContext.getTranScope();
        if (!ObjectUtils.isEmpty(tranScope.get(atomTranParam))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水作用域已存在,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("流水作用域已存在,请检查参数配置[{}]", atomTranParam), null);
        }
        // 获取流程单元中的参数列表并进行处理
        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("增强处理参数未配置"), null);
        }
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit);
        verifyQueryParam(reqScopeValUnit);
        try {
            // 定义数据库实体类对象
            TEbJournalPo tEbJournalPo;
            // 查询流水表主表内容
            tEbJournalPo = journalService.selectJournalInfo(reqScopeValUnit);
            // 流水表内容写入返回数据集
            if (!ObjectUtils.isEmpty(tEbJournalPo)) {
                count = 1;
                PbScope<Object> journalScope = JournalUtils.buildJournalScope(tEbJournalPo, new PbScope<>(), OPERATE_TYPE_S);
                // 流水写入tranScope
                MapUtils.setVal(tranScope, atomTranParam, journalScope);
                LOGGER.debug("当前流水作用域 [{}],内容 [{}]", "tran." + atomTranParam, journalScope);
            } else {
                count = 0;
                LOGGER.debug("未查询到流水");
            }
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "流水信息查询异常,异常信息[{}]", e.getMessage(), e);
            atomResult.setRetType(RetType.FAILED);
            return atomResult;
        } finally {
            LOGGER.info("流水查询完成,size[{}]", count);
            scopeValUnit.put(RESULT_COUNT, count);
        }
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

    /**
     * 验证查询参数是否有效
     * 该方法检查给定的ScopeValUnit对象中的参数是否都包含在预定义的索引集合中，
     * 如果存在不在索引集合中的参数，则记录错误日志并抛出异常
     *
     * @param scopeValUnit 包含查询参数的ScopeValUnit对象，不能为空
     */
    private void verifyQueryParam(ScopeValUnit scopeValUnit) {
        // 使用 Set.of 创建不可变集合
        Set<String> indexSet = ImmutableSet.of(REQ_CHNL, REQ_DATE, BUSI_KEY, TRAN_DATE, INNER_BUSI_KEY, REQ_TIME, TRAN_STATE, ACCT_NO, AUTH_CODE, MER_CODE);

        StringBuilder stringBuilder = new StringBuilder();
        //遍历scopeValUnit的键值，判断键值key是否在HashSet内，如果不在进行记录，循环完毕后进行日志输出
        for (String key : scopeValUnit.keySet()) {
            if (!indexSet.contains(key)) {
                //如果key不在HashSet内，使用stringBuilder以逗号分割的形式记录所有的key,循环完毕后使用统一进行输出
                stringBuilder.append(key).append(",");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "不支持下列参数进行流水信息查询,请检查![{}]", stringBuilder);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("不支持下列参数进行流水信息查询,请检查![{}]", stringBuilder.toString()));
        }
    }
}
