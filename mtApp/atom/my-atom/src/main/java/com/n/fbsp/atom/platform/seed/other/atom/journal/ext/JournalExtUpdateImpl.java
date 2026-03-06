package com.n.fbsp.atom.platform.seed.other.atom.journal.ext;

import com.n.fbsp.atom.platform.seed.other.service.IJournalExtService;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 原子交易参数：
 * 无需配置
 * <p>
 * 增强处理参数：key|value
 * busiKey =#{XXX.busiKey}必输
 * reqDate =#{XXX.reqDate}必输
 * reqChnl =#{XXX.reqChnl}必输
 * 其他要更新的字段
 * traceId|${req.trace}
 * reqId|${req.id}
 *
 * @Author : 
 * @create 2024/2/28 14:18
 */
@Atom("fbsp.JournalExtUpdate")
public class JournalExtUpdateImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JournalExtUpdateImpl.class);

    @Autowired
    private IJournalExtService iJournalExtService;

    /**
     * 原子交易主要逻辑实现
     *
     * @param tranContext 交易上下文，包含交易流程的全局信息
     * @param scopeValUnit 本单元上下文对象
     * @param flowUnit     流程单元，包括配置参数信息
     * @return Result结果
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化原子交易结果对象
        AtomResult atomResult = new AtomResult();
        // 初始化操作计数器
        /*int count;

        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数未配置"));
        }
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit, false);
        JournalExt journalExt;
        String busiKey = (String) reqScopeValUnit.get(BUSI_KEY);
        String reqDate = (String) reqScopeValUnit.get(REQ_DATE);
        String reqChnl = (String) reqScopeValUnit.get(REQ_CHNL);
        if (ObjectUtils.isEmpty(busiKey) || ObjectUtils.isEmpty(reqDate) || ObjectUtils.isEmpty(reqChnl)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数必输项[busiKey、reqDate、reqChnl]未配置,请检查");
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数必输项[busiKey、reqDate、reqChnl]未配置,请检查"));
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put(BUSI_KEY, busiKey);
            params.put(REQ_DATE, reqDate);
            params.put(REQ_CHNL, reqChnl);
            journalExt = iJournalExtService.selectJournalExt(params);
        }
        if (ObjectUtils.isEmpty(journalExt)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未查询到辅助流水表信息,请检查参数是否正确busiKey[{}]、reqDate[{}]、reqChnl[{}]", busiKey, reqDate, reqChnl);
        }
        journalExt = BeanMapUtil.mapToBean(reqScopeValUnit, journalExt);
        try {
            // 执行更新操作
            count = iJournalExtService.updateJournalExt(journalExt);
        } catch (Exception e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "更新流水扩展表异常,异常信息 [{}]", e.getMessage(), e);
            // 更新操作失败时，设置count为-1，方便后续处理失败标记
            count = -1;
            // 将失败标记更新到作用单元中
            scopeValUnit.put(RESULT_COUNT, count);
            // 设置结果为失败，并返回结果对象
            atomResult.setRetType(RetType.FAILED);
            LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
            return atomResult;
        }
        LOGGER.info("扩展流水更新完成,size[{}]", count);
        // 将操作计数器存入作用域
        scopeValUnit.put(RESULT_COUNT, count);*/
        atomResult.setRetType(RetType.SUCCESS);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);
        return atomResult;
    }
}
