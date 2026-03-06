package com.nantian.nbp.flow.engine.service.api.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * @Author : Liang Haizhen
 * @create 2023/4/14 15:15
 */
public enum ErrorCodeEnum {
    /**
     * 与%s系统通讯异常
     */
    T_N0001091("T-N0001091", "通讯异常"),
    T_N0001092("T-N0001092", "内部异常"),
    T_N0001093("T-N0001093", "服务方异常"),
    T_N0001094("T-N0001094", "其他HTTP请求异常"),
    /**
     * 访问%s数据库异常
     */
    T_D0002091("T-D0002091", "数据库异常"),
    /**
     * 报文解析异常
     */
    T_C0003091("T-C0003091", "报文解析异常"),
    /**
     * 数据参数不合法
     */
    T_C0003092("T-C0003092", "数据参数不合法"),

    /**
     * JsonSchema校验失败异常
     */
    T_C0003093("T-C0003093", "JsonSchema校验异常"),

    /**
     * 配置数据不合法
     */
    T_C0003094("T-C0003094", "配置参数不合法"),


    T_C0003095("T-C0003095", "数据库事务异常"),
    /**
     * PIN转换失败
     */
    T_S0004091("T-S0004091", "PIN转换失败"),
    /**
     * 应用网关加验签等处理异常
     */
    T_S0004092("T-S0004092", "应用网关处理异常"),
    /**
     * 其他捕获异常
     */
    T_X0005091("T-X0005091", "其他捕获异常");

    private final String code;
    private final String codeMsg;

    /**
     * 构造方法
     *
     * @param code    错误码
     * @param codeMsg 错误信息
     */
    ErrorCodeEnum(String code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
    }

    public String getCode() {
        return code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public String getCodeMsg(String errMsg, Object... args) {
        return codeMsg +","+MessageFormatter.arrayFormat(errMsg, args).getMessage();
    }
}
