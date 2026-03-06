package com.n.fbsp.atom.platform.seed.db.seq;

public class SeqNumException extends RuntimeException{

    private String code;
    private String msg;

    public SeqNumException(Exception e) {
        super(e);
    }
    public SeqNumException(String message) {
        super(message);
        // 默认错误码值
        this.code = "999999";
        this.msg = message;
    }

    public SeqNumException(String code,String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
