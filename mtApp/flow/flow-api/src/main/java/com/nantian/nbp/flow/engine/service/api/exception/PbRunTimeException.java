package com.nantian.nbp.flow.engine.service.api.exception;

/**
 * 用于流程中由TRY捕获的特定异常类型
 * */
public class PbRunTimeException extends RuntimeException {

    private final String errCode;
    private final String msg;
    private String errStep;

    public PbRunTimeException(String errCode) {
        this(errCode, null);
    }

    public PbRunTimeException(String errCode, String msg) {
        super(msg);
        this.errCode = errCode;
        this.msg = msg;
    }
    public PbRunTimeException(String errCode, String msg, Throwable e) {
        super(msg,e);
        this.errCode = errCode;
        this.msg = msg;
    }

    public PbRunTimeException(String errCode, String msg, String errStep,Throwable e) {
        super(msg,e);
        this.errCode = errCode;
        this.msg = msg;
        this.errStep = errStep;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getMsg() {
        return msg;
    }

    public String getErrStep() {
        return errStep;
    }

    public void setErrStep(String errStep) {
        this.errStep = errStep;
    }
}
