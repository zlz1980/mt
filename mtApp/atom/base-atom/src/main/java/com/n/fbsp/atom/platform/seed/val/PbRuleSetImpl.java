package com.n.fbsp.atom.platform.seed.val;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.base.model.UtilsBean;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.ev.PbEvContext;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.rule.RuleService;
import com.nantian.nbp.rule.entity.Rule;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.Constants.APP_ATOM_RUN_ERR_KEY;
import static com.nantian.nbp.flow.engine.service.api.Constants.INIT_RET_VAL;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_NUM_TWO;
import static com.nantian.nbp.flow.engine.service.api.Constants.PARAM_SPILT_FLAG;
import static com.nantian.nbp.flow.engine.service.api.Constants.RESULT_COUNT;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.CTX_FLAG;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.IN_HEADER_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.IN_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.MAP_INIT;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.SYS_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.FeConstants.TRAN_SCOPE;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_X0005091;

/**
 * 规则组操作，根据原子交易参数配置，获取到指定的规则组信息，
 * 原子交易的增强处理参数作为规则组的匹配数据源，匹配成功后将匹配结果写入到当前作用域的指定的key中。
 *
 * @author Administrator
 */
@Atom("base.PbRuleSet")
public class PbRuleSetImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbRuleSetImpl.class);

    private static final int RULE_SUCCESS = 1;
    private static final int RULE_FAIL = 0;

    private final RuleService ruleService;
    @Autowired
    private CacheClientApi cacheClientApi;

    public PbRuleSetImpl(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * 1.原子交易参数（以 | 分割）：
     * 规则组类型|匹配结果Key
     * 规则组类型:对应规则组配置页面的类型列
     * 匹配结果Key:匹配结果写入对应的Key
     * 2.增强处理参数：
     * 匹配条件Key1|匹配条件Val1
     * 匹配条件Key2|匹配条件Val2
     * 匹配条件Key3|匹配条件Val3
     * 匹配条件Keyn|匹配条件Valn
     * sys、tran、inHeader、in作用域默认传入，无需在增强处理参数中配置
     *
     * @param tranContext
     * @param scopeValUnit 本单元上下文对象
     * @param flowUnit     流程单元，包括配置参数信息
     * @return Result 单元结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || Objects.equals(INIT_RET_VAL, flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取流程单元中的原子交易参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] param = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (param.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam));
        }
        List<String> paras = flowUnit.getParamList();
        if (!CollectionUtils.isEmpty(paras)) {
            for (String para : paras) {
                FlowParaUtils.setFlowParaValByValid(tranContext, scopeValUnit, para, true);
            }
        }
        try {
            PbScope<Object> props = new PbScope<>(MAP_INIT);
            props.put(SYS_SCOPE, tranContext.getSysScope());
            props.put(TRAN_SCOPE, tranContext.getTranScope());
            props.put(IN_SCOPE, tranContext.getInScope());
            props.put(IN_HEADER_SCOPE, tranContext.getInHeaderScope());
            props.putAll(scopeValUnit);
            PbEvContext evCtx = new PbEvContext();
            evCtx.setVariable(CTX_FLAG, props);
            setUtilsBean(evCtx);
            Rule resRule = ruleService.findRule(tranContext.getValue(StrUtils.trim(param[0])).toString(), evCtx);
            scopeValUnit.clear();
            if (Objects.nonNull(resRule) && StringUtils.hasText(resRule.getResult())) {
                String res = resRule.getResult();
                LOGGER.info("规则组匹配成功,匹配结果[{}]:[{}]", param[1], res);
                scopeValUnit.put(StrUtils.trim(param[1]), res);
                scopeValUnit.put(RESULT_COUNT, RULE_SUCCESS);
            } else {
                scopeValUnit.put(RESULT_COUNT, RULE_FAIL);
            }
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "规则组匹配异常,异常信息[{}]", e.getMessage());
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("规则组匹配异常,异常信息[{}]", e.getMessage()), e);
        }
        atomResult.setRetType(RetType.SUCCESS);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }

    /**
     * 设置工具类Bean到上下文中
     * <p>
     * 此方法从缓存中获取工具类Bean，并将它们设置到上下文中
     * 如果某个工具类Bean的名称为"ctx"，则抛出异常，因为这会导致上下文变量命名冲突
     *
     * @param evCtx 上下文对象，用于存储和传递处理过程中所需的上下文信息
     */
    private void setUtilsBean(PbEvContext evCtx) {
        // 从缓存客户端API获取工具类Bean集合
        PbResourceCache<UtilsBean> utilsBeanSet = cacheClientApi.getUtilsBeans();

        // 检查工具类Bean集合是否非空
        if (Objects.nonNull(utilsBeanSet)) {
            // 遍历工具类Bean集合
            for (UtilsBean utilsBean : utilsBeanSet.values()) {
                // 检查工具类Bean的名称是否为"ctx"，如果是，则抛出异常
                if (CTX_FLAG.equals(utilsBean.getBeanName())) {
                    throw new RuntimeException("bean name can not be 'ctx'");
                }
                // 将工具类Bean设置到事件上下文中
                evCtx.setVariable(utilsBean.getBeanName(), utilsBean.getBean());
            }
        }
    }

}
