package com.nantian.nbp.flow.engine.service;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.flow.engine.service.api.ExceptionResHandler;
import com.nantian.nbp.flow.engine.service.api.FeConstants;
import com.nantian.nbp.flow.engine.service.api.exception.FlowException;
import com.nantian.nbp.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 对异常对象进行JSON格式处理
 *
 * @author Administrator
 */
public class DefExceptionJsonResImpl implements ExceptionResHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefExceptionJsonResImpl.class);

    /**
     * 处理异常响应的方法
     * 当业务处理中发生异常时，通过此方法构建异常响应信息，并记录日志
     *
     * @param bizId    业务标识
     * @param e        异常对象
     * @param tcc      交易码转换对象，用于获取前端交易码
     * @param response HTTP响应对象，用于发送响应
     * @param errType  错误类型
     * @param pbLog    日志对象，用于记录响应日志
     * @return 返回构建的异常响应JSON字符串
     */
    @Override
    public String exceptionRes(String bizId, Exception e, TranCodeConv tcc, Object response,
            String errType, PbLog pbLog) {
        // 初始化响应结果Map，容量为5，避免频繁扩容
        Map<String, Object> resMap = new HashMap<>();
        // 默认错误码为EFLW001
        String errCode = FlowException.EFLW001;
        // 如果异常是FlowException类型，则使用FlowException的错误码
        if (e instanceof FlowException) {
            FlowException fe = (FlowException) e;
            errCode = fe.getErrCode();
        }
        if (!ObjectUtils.isEmpty(pbLog)) {
            // 设置日志的响应码和响应消息
            pbLog.setRespCode(errCode);
            pbLog.setRespMsg(e.getMessage());
        }
        // 将错误码、前端交易码和错误消息放入响应结果Map中
        resMap.put(FeConstants.ERR_CODE, errCode);
        resMap.put(FeConstants.F_TRAN_CODE, tcc.getfTranCode());
        resMap.put(FeConstants.ERR_MSG, e.getMessage());
        // 将响应结果Map转换为JSON字符串
        String res;
        try {
            res = JsonUtil.writeValueAsString(resMap);
        } catch (Exception ex) {
            // 如果转换过程中发生异常，记录错误日志，并返回默认错误响应
            LOGGER.error("", ex);
            res = "{\"errCode\":\"1001\",\"message\":\"JsonProcessingException\"}";
        }
        // 返回构建的异常响应JSON字符串
        return res;
    }

}
