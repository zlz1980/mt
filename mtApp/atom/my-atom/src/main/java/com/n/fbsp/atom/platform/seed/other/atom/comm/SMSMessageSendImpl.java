package com.n.fbsp.atom.platform.seed.other.atom.comm;

import com.n.fbsp.atom.platform.seed.other.atom.mq.SMSMqMessageProducer;
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
 * 发送消息平台，多次重试如果消息平台异常则落库
 * 原子交易参数：
 * 渠道|交易码|业务主键
 * <p>
 * 增强处理参数：key|value
 * traceId|${req.trace}
 * reqId|${req.id}
 *
 * @Author : 
 * @Date 2024/8/29 11:19
 **/
@Atom("fbsp.SMSMessageSend")
public class SMSMessageSendImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSMessageSendImpl.class);
    @Autowired
    SMSMqMessageProducer smsMqMessageProducer;
//    @Value("${mq.producer.topicSms}")
    private String topic;

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象，预设为成功
        AtomResult atomResult = new AtomResult();
        /*if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()), null);
        }
        // 获取流程参数字符串 渠道|交易码
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_THREE) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("原子交易参数个数不为3,请检查参数配置[{}]", atomTranParam), null);
        }
        // 解析参数：渠道|交易码|业务ID
        String chnlNo = MapUtils.spiltVal(atomTranParams[0]);
        String fTranCode = MapUtils.spiltVal(atomTranParams[1]);
        String bizId = (String) tranContext.getCtxVal(atomTranParams[2]).getVal();
        Map<String, String> properties = new HashMap<>();
        properties.put(CHNL_NO, chnlNo);
        properties.put(F_TRAN_CODE, fTranCode);
        // 获取增强处理多参
        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), tranContext.getFeTranCode(), T_C0003092.getCodeMsg("增强处理参数未配置"), null);
        }
        // 日志输出增强处理参数信息
        LOGGER.debug("增强处理参数[{}]", JsonUtil.objToString(paramList));
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit);
        MqMessage MqMessage = new MqMessage();
        MqMessage.setProperties(properties);
        MqMessage.setMsgId(bizId);
        MqMessage.setBody(JsonUtil.objToString(reqScopeValUnit).getBytes());
        smsMqMessageProducer.addMessage(MqMessage);
        LOGGER.info("短信消息添加至消息队列成功,将通过异步线程发送至消息平台！TOPIC[{}],消息ID:[{}]", topic, bizId);*/
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}
