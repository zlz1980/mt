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
 * innerBusiKey生成
 * 基于分布式体系32位业务主键规范生成
 * 与busiKey一一对应，用于行内服务方调用、行内对账
 *
 * @Author : 
 * @Date 2024/8/29 11:19
 **/
@Atom("fbsp.InnerBusiKeyGenerate")
public class InnerBusiKeyGenerateImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InnerBusiKeyGenerateImpl.class);
    private static final String SEQ_GLOBAL = "seq_Global";
    private static final String INNER_BUSI_KEY = "innerBusiKey";
    private static final String CLOUD_RESERVE_KEY = "099a";
    /*@Autowired
    SequenceClient sequenceClient;*/

    /**
     * 根据提供的参数执行服务，并返回处理结果。
     *
     * @param tranContext   流程上下文
     * @param scopeValUnit 当前作用域
     * @param flowUnit     流程单元，暂未在方法内部使用，但可能是流程中的重要信息。
     * @return AtomResult 返回一个代表服务执行结果的对象，预设为成功。
     */
    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象
        AtomResult atomResult = new AtomResult();
        // 获取当前日期并用作部分业务键的内容
        /*String date = CalendarUtil.getDate();
        Long sequenceCode;
        String busiKey;
        try {
            sequenceCode = sequenceClient.nextId(SEQ_GLOBAL, 0);
            String sequenceNo = sequenceCode.toString();
            //判断sequenceCode长度，超过八位截取后八位，不足八位补齐8位
            if (sequenceNo.length() > 8) {
                sequenceNo = sequenceNo.substring(sequenceNo.length() - 8);
            } else {
                sequenceNo = String.format("%08d", sequenceCode);
            }
            // 构建业务键，依据日期和随机数，加上固定的字符串操作,099a兼容云下场景
            busiKey = AMS_FBSP_CODE + FBSP_CHNLNO + date + RESERVED + CLOUD_RESERVE_KEY + sequenceNo;
            LOGGER.info("业务主键生成成功,innerBusiKey[{}]", busiKey);
        } catch (SequenceException e) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "生成业务主键异常,异常信息[{}]", e.getMessage(), e);
            //调用异常时的降级处理
            throw new FlowException(T_X0005091.getCode(), tranContext.getFeTranCode(), T_X0005091.getCodeMsg("生成业务主键异常,异常信息[{}]", e.getMessage()), e);
        }
        // 将业务键保存到上下文对象中，以innerBusiKey为键
        MapUtils.setVal(scopeValUnit, INNER_BUSI_KEY, busiKey);
        LOGGER.debug("当前作用域信息 [{}]", scopeValUnit);*/
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }

}

