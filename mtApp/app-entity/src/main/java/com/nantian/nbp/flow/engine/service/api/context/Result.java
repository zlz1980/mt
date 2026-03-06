package com.nantian.nbp.flow.engine.service.api.context;

import java.util.Objects;

/**
 * @author Administrator
 */
public class Result {
    /**
     * 原子交易的执行结果
     */
    private RetType retType = RetType.UNKNOWN;

    /**
     * 备注信息
     */
    private String msg;

    public Result() {
    }

    public Result(RetType retType) {
        this.retType = retType;
    }

    public RetType getRetType() {
        return retType;
    }

    public void setRetType(RetType retType) {
        this.retType = retType;
    }

    public void setRetType(boolean flag) {
        if (flag) {
            this.retType = RetType.SUCCESS;
        } else {
            this.retType = RetType.FAILED;
        }
    }

    // 执行是否成功
    public boolean isSuccess() {
        return Objects.equals(RetType.SUCCESS, getRetType());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
