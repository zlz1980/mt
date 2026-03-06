package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PbLog设置新key值，用于批转联saga,mqsms这种重放原交易进行补偿类的交易
 * 避免出现重复使用同一个key值，导致日志无法正常记录
 * <p>
 * 原子交易参数配置
 * ${bizID}|${seqNum}
 * 增强处理和错误信息错误码无需配置
 *
 * @author 
 */
@Atom("fbsp.PbLogKeyReset")
public class PbLogKeyResetImpl implements AtomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbLogKeyResetImpl.class);

    /**
     * 执行服务方法，用于处理原子交易流程
     *
     * @param tranContext  交易上下文，包含交易过程中需要的各种信息
     * @param scopeValUnit 作用域值单元，用于管理不同作用域下的值
     * @param flowUnit     流程单元，包含流程执行所需的参数和配置
     * @return AtomResult 返回原子交易的执行结果
     *
     * @throws FlowException 流程异常，当流程参数不满足要求时抛出
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化原子结果对象
        AtomResult atomResult = new AtomResult();
        // 校验原子交易参数是否为空或初始值
        /*if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取流程参数字符串
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam));
        }
        // 从交易上下文中获取业务ID和序列号
        String bizId = (String) tranContext.getCtxVal(StrUtils.trim(atomTranParams[0])).getVal();
        String sequence = (String) tranContext.getCtxVal(StrUtils.trim(atomTranParams[1])).getVal();
        // 获取系统级别的作用域对象
        PbScope<Object> sysMap = tranContext.getSysScope();
        // 获取日志对象
        PbLog pbLog = (PbLog) sysMap.get(PB_LOG);
        // 组装新的业务ID
        String newBizId = bizId.concat("-").concat(sequence);
        // 设置日志对象的新业务ID
        pbLog.setBizId(newBizId);
        // 获取系统级别的作用域对象
        PbHeader inHeaderMap = tranContext.getInHeaderScope();
        //同步修改网关送过来的fbsp-busikey
        String fbspBusiKey = inHeaderMap.get(FBSP_BUSI_KEY);
        if (StringUtils.hasText(fbspBusiKey)) {
            inHeaderMap.put(FBSP_BUSI_KEY, newBizId);
        }
        // 记录日志
        LOGGER.info("PbLog设置新bizId完成[{}]", newBizId);*/
        // 设置原子结果为成功
        atomResult.setRetType(RetType.SUCCESS);
        // 返回原子结果
        return atomResult;
    }
}
