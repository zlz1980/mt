package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.TranCodeConv;

/**
 * 扩展对异常对象处理返回信息接口
 *
 * @author JiangTaiSheng
 */
public interface ExceptionResHandler {


    /**
     * 处理业务异常情况，生成异常响应结果
     *
     * @param bizId    业务标识ID，用于唯一标识一次业务操作
     * @param e        异常对象，表示在业务处理过程中捕获的异常
     * @param tcc      交易码转换对象，用于处理交易码相关的转换逻辑
     * @param response HTTP响应对象，用于设置响应信息
     * @param errType  错误类型，用于标识错误的类别
     * @param pbLog    日志对象，用于记录日志信息
     * @return 返回处理后的异常响应结果字符串
     */
    String exceptionRes(String bizId, Exception e, TranCodeConv tcc, Object response, String errType,
            PbLog pbLog);
}
