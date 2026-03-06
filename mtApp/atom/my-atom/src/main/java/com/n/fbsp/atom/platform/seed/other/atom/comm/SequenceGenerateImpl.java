package com.n.fbsp.atom.platform.seed.other.atom.comm;


import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列生成
 * 原子交易参数配置
 * key|bizValue|序列长度
 * 举例：
 * traceNo|'seq_cfps'|6
 *
 * @Author : 
 * @Date 2024/11/26 16:49
 **/
@Atom("fbsp.SequenceGenerate")
public class SequenceGenerateImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceGenerateImpl.class);
//    @Autowired
//    SequenceClient sequenceClient;

    /**
     * 根据提供的参数执行服务，并返回处理结果。
     *
     * @param tranContext
     * @param scopeValUnit 范围值单元，用于上下文的参数和结果传递。
     * @param flowUnit     流程单元，暂未在方法内部使用，但可能是流程中的重要信息。
     * @return AtomResult 返回一个代表服务执行结果的对象，预设为成功。
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象，预设为成功
        AtomResult atomResult = new AtomResult();

        /*if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程传输参数
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }
        String key = StrUtils.trim(atomTranParams[0]);
        // 解析参数：bizValue
        String bizValue = (String) tranContext.getCtxVal(StrUtils.trim(atomTranParams[1])).getVal();
        int length = Integer.parseInt(StrUtils.trim(atomTranParams[2]));
        Long sequenceCode;
        String sequenceNo;
        try {
            sequenceCode = sequenceClient.nextId(bizValue, 0);
            sequenceNo = sequenceCode.toString();
            //生成的序列号长度大于配置的序列长度，需要截取
            if (sequenceNo.length() > length) {
                sequenceNo = sequenceCode.toString().substring(sequenceCode.toString().length() - length);
            } else {
                // 转为字符串并生成序列号
                String format = "%0" + length + "d";
                sequenceNo = String.format(format, sequenceCode);
            }
        } catch (SequenceException e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "生成序列异常,异常信息[{}]", e.getMessage(), e);
            //调用异常时的降级处理
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("生成序列异常,异常信息[{}]", e.getMessage()), e);
        }
        LOGGER.info("序列生成成功:[{}]", sequenceNo);
        // 将序列号写入到上下文对象中
        MapUtils.setVal(scopeValUnit, key, sequenceNo);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);*/
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}

