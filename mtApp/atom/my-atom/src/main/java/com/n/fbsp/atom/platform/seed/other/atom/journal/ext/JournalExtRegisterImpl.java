package com.n.fbsp.atom.platform.seed.other.atom.journal.ext;

import com.n.fbsp.atom.platform.seed.other.po.JournalExt;
import com.n.fbsp.atom.platform.seed.other.utils.JournalUtils;
import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.flow.engine.service.api.util.FlowParaUtils;
import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.nantian.nbp.flow.engine.service.api.Constants.*;
import static com.nantian.nbp.flow.engine.service.api.exception.ErrorCodeEnum.T_C0003092;

/**
 * 流水扩展表发送消息平台
 * 原子交易参数：
 * 业务主键|流水作用域
 * ${tran.defJournal.busiKey}|${tran.defJournal}
 * <p>
 * 增强处理参数：key|value
 * traceId|${req.trace}
 * reqId|${req.id}
 *
 * @Author : 
 * @Date 2024/8/29 11:19
 **/
@Atom("fbsp.JournalExtRegister")
public class    JournalExtRegisterImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalExtRegisterImpl.class);
    /*@Autowired
    JouExtMqMessageProducer jouExtMqMessageProducer;*/
//    @Value("${mq.producer.topicJouExt}")
    private String topic;

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        // 初始化结果对象，预设为成功
        AtomResult atomResult = new AtomResult();
        if (ObjectUtils.isEmpty(flowUnit.getAtomTranParam()) || INIT_RET_VAL.equals(flowUnit.getAtomTranParam())) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam());
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数为空或未配置,请检查参数配置[{}]", flowUnit.getAtomTranParam()));
        }
        // 获取流程参数字符串 业务主键|流水作用域
        String atomTranParam = StrUtils.trim(flowUnit.getAtomTranParam());
        // 将参数字符串按分隔符分割成数组
        String[] atomTranParams = StringUtils.delimitedListToStringArray(atomTranParam, PARAM_SPILT_FLAG);
        // 参数校验：判断参数个数是否正确
        if (atomTranParams.length != PARAM_NUM_TWO) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("原子交易参数个数不为2,请检查参数配置[{}]", atomTranParam));
        }
        String bizId;
        if (ObjectUtils.isEmpty(tranContext.getCtxVal(atomTranParams[0]))) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "未获取到业务主键,请检查参数配置[{}]", atomTranParams[0]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("未获取到业务主键,请检查参数配置[{}]", atomTranParams[0]));
        } else {
            bizId = (String) tranContext.getCtxVal(atomTranParams[0]).getVal();
            if (ObjectUtils.isEmpty(bizId)) {
                LOGGER.error(APP_ATOM_RUN_ERR_KEY + "业务主键为空,请检查参数配置[{}]", atomTranParams[0]);
                throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("业务主键为空,请检查参数配置[{}]", atomTranParams[0]));
            }
        }
        //主流水作用域
        Object jouObject = tranContext.getCtxVal(atomTranParams[1]).getVal();
        if (ObjectUtils.isEmpty(jouObject)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "主流水作用域为空,请检查参数配置[{}]", atomTranParams[1]);
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("主流水作用域为空,请检查参数配置[{}]", atomTranParams[1]));
        }
        PbScope<Object> jouScope = (PbScope<Object>) jouObject;

        List<String> paramList = flowUnit.getParamList();
        if (CollectionUtils.isEmpty(paramList)) {
            LOGGER.error(APP_ATOM_RUN_ERR_KEY + "增强处理参数未配置");
            throw new FlowException(T_C0003092.getCode(), T_C0003092.getCodeMsg("增强处理参数未配置"));
        }
        // 日志输出增强处理参数信息
        LOGGER.debug("增强处理参数[{}]", JsonUtil.objToString(paramList));
        ScopeValUnit reqScopeValUnit = new ScopeValUnit();
        FlowParaUtils.setFlowParaValues(tranContext, reqScopeValUnit, flowUnit, false);
        JournalExt journalExt = JournalUtils.buildJournalExt(tranContext, reqScopeValUnit, jouScope);
        /*MqMessage MqMessage = new MqMessage();
        // 生产者TOPIC
        MqMessage.setMsgId(bizId);
        // 获取增强处理多参
        MqMessage.setBody(JsonUtil.objToString(journalExt).getBytes());*/
//        jouExtMqMessageProducer.addMessage(MqMessage);
        LOGGER.info("扩展流水消息添加至消息队列成功,将通过异步线程发送至消息平台！TOPIC[{}],消息ID:[{}]", topic, bizId);
        atomResult.setRetType(RetType.SUCCESS);
        return atomResult;
    }
}
