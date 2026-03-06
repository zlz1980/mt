package com.n.fbsp.atom.platform.seed.other.atom.journal;

import com.google.common.collect.Sets;


import com.n.fbsp.atom.platform.seed.other.po.TEbJournalPo;
import com.n.fbsp.atom.platform.seed.other.utils.JournalUtils;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.ev.MapUtils;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;


import static com.n.fbsp.atom.platform.seed.other.utils.Constants.OPERATE_TYPE_S;
import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * @author 
 * @date 2025/12/8
 * @description 流水查询原子服务基类，定义流水查询的通用流程与接口规范，包括参数校验、查询逻辑与结果处理。
 */
public abstract class AbstractJournalSelect implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJournalSelect.class);

    /**
     * 获取查询参数集合
     * <p>
     * 此抽象方法要求子类实现，用于返回一个字符串集合，这些字符串代表了查询操作中使用的参数
     * 这种设计允许不同的查询条件以灵活的方式被不同子类实现，而无需修改父类代码
     *
     * @return Set<String> 包含查询参数的集合
     */
    protected abstract Set<String> getQueryParam();

    /**
     * 根据作用域值单元查询流水信息
     *
     * @param scopeValUnit 作用域值单元，包含查询所需的参数信息
     * @return 流水信息实体对象，如果未查询到则返回null
     */
    protected abstract TEbJournalPo selectJournalInfo(ScopeValUnit scopeValUnit);

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
            // 查询流水表主表内容
            TEbJournalPo tEbJournalPo = selectJournalInfo(reqScopeValUnit);
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
     * 验证查询参数是否完整且合法
     *
     * @param scopeValUnit 包含待验证查询参数的ScopeValUnit对象
     */
    private void verifyQueryParam(ScopeValUnit scopeValUnit) {
        // 获取必需的查询参数集合
        Set<String> requiredParams = getQueryParam();
        // 检查是否包含所有必需参数
        if (!scopeValUnit.keySet().containsAll(requiredParams)) {
            // 找出缺少的参数
            Set<String> missingParams = Sets.difference(requiredParams, scopeValUnit.keySet());
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "查询参数缺失,缺少必需参数: [{}]", String.join(PARAM_COMMA_SPILT, missingParams));
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("查询参数缺失,缺少必需参数: [{}]", String.join(PARAM_COMMA_SPILT, missingParams)));
        }
        // 检查是否存在不支持的参数
        Set<String> unsupportedParams = Sets.difference(scopeValUnit.keySet(), requiredParams);
        if (!unsupportedParams.isEmpty()) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "不支持下列参数进行流水信息查询,请检查![{}]", String.join(PARAM_COMMA_SPILT, unsupportedParams));
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("不支持下列参数进行流水信息查询,请检查![{}]", String.join(PARAM_COMMA_SPILT, unsupportedParams)));
        }
    }
}
