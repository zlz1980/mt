package com.nantian.nbp.base.model;

import java.io.Serializable;

public class Api implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 渠道
     */
    private String busiType;
    /**
     * 交易流程号
     */
    private String apiCode;
    /**
     * API接口类型
     */
    private String jsType;
    /**
     * API接口定义
     */
    private String jsInfo;

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getJsType() {
        return jsType;
    }

    public void setJsType(String jsType) {
        this.jsType = jsType;
    }

    public String getJsInfo() {
        return jsInfo;
    }

    public void setJsInfo(String jsInfo) {
        this.jsInfo = jsInfo;
    }
}
