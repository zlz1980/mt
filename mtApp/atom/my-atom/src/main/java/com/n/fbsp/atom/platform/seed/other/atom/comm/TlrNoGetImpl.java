package com.n.fbsp.atom.platform.seed.other.atom.comm;


import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 
 * @date 2025/11/28
 * @description 取虚拟柜员号
 * <p>
 * 原子交易参数：
 * 组号(支持变量和常量，int类型不带'')
 * <p>
 * 增强处理参数：无需配置
 */
@Atom("fbsp.TlrNoGet")
@Component
public class TlrNoGetImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TlrNoGetImpl.class);
    private static final String SEQ_TLR_NO = "seq_TlrNo_";
    private static final String T_PB_TLRNOCFG_VALUE = "t_pb_tlrnocfg_value";
    private static final String T_PB_TLRNOCFG_GROUP = "t_pb_tlrnocfg_group";
    private static final String TLR_RES_VALUE = "tlrresno";
    private static final String GROUP_SUM_NO = "groupsumno";

//    @Autowired
//    SequenceClient sequenceClient;
    @Autowired
    private CacheClientApi cacheClientApi;

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
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        // 提取参数，判断是否初始化
        /*if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (atomTranParams.length != PARAM_NUM_ONE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为1,请检查参数配置[{}]", atomTranParam), null);
        }
        String groupIdStr = (String) tranContext.getValue(atomTranParams[0]);
        if (!StringUtils.hasText(groupIdStr)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到组ID参数值,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("未获取到组ID参数值,请检查参数配置[{}]", atomTranParam), null);
        }
        int groupId = Integer.parseInt(groupIdStr);
        // 缓存中获取组中柜员总量
        Map<String, Object> tlrGroupMap = cacheClientApi.getStaticTable(T_PB_TLRNOCFG_GROUP, groupIdStr);
        int groupSumNo = Integer.parseInt((String) tlrGroupMap.get(GROUP_SUM_NO));
        if (groupSumNo == 0) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "资源配置异常,当前组ID[{}]下无数据,请检查[T_PB_TLRNOCFG]表配置", groupId);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("资源配置异常,当前组ID[{}]下无数据,请检查[T_PB_TLRNOCFG]表配置", groupId), null);
        }
        // 获取序列值
        Long sequenceCode;
        try {
            sequenceCode = sequenceClient.nextId(SEQ_TLR_NO + groupId, 0);
        } catch (SequenceException e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "生成序列异常,异常信息[{}]", e.getMessage(), e);
            //调用异常时的降级处理
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("生成序列异常,异常信息[{}]", e.getMessage()), e);
        }
        int seqNo = Math.toIntExact(sequenceCode);
        // 计算序号
        int tlrResNo = seqNo % groupSumNo;
        // 避免有0的情况，统一+1
        tlrResNo++;
        // 缓存中获取虚拟柜员号
        String cacheKey = groupId + "_" + tlrResNo;
        Map<String, Object> tlrResMap = cacheClientApi.getStaticTable(T_PB_TLRNOCFG_VALUE, cacheKey);
        String tlrResValue = tlrResMap.get(TLR_RES_VALUE).toString();
        // 数据库查询结果信息处理并写入当前作用域
        scopeValUnit.put("tlrNo", tlrResValue);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);*/
        // 设置处理结果为成功
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}