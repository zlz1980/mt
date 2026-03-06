/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.TranCodeConv;

import java.util.Map;

/**
 * 执行配置类
 *
 * @author Administrator
 */
public class ExecuteConfig {
    /** 渠道号 */
    private final String chnlNo;
    /** 外部交易码 */
    private final String fTranCode;
    /** 业务种类 */
    private final String bizType;
    /** 内部交易码 */
    private final String tranCode;
    /** 请求报文 */
    private final String reqData;
    /** 业务编号 */
    private String bizId = null;
    /** 内外交易码映射 */
    private TranCodeConv tranCodeConv = null;
    /** 请求报文头 */
    private Map<String, String> reqHeader;

    public ExecuteConfig(TranCodeConv tranCodeConv, String reqData, Map<String, String> reqHeader) {
        this.tranCodeConv = tranCodeConv;
        this.chnlNo = tranCodeConv.getChnlNo();
        this.fTranCode = tranCodeConv.getfTranCode();
        this.bizType = tranCodeConv.getBusiType();
        this.tranCode = tranCodeConv.getTranCode();
        this.reqData = reqData;
        this.reqHeader = reqHeader;
    }

    public ExecuteConfig(String chnlNo, String fTranCode, String bizType, String tranCode, String reqData, Map<String
            , String> reqHeader) {
        this.chnlNo = chnlNo;
        this.fTranCode = fTranCode;
        this.bizType = bizType;
        this.tranCode = tranCode;
        this.reqData = reqData;
        this.reqHeader = reqHeader;
    }


    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getChnlNo() {
        return chnlNo;
    }

    public String getfTranCode() {
        return fTranCode;
    }

    public String getBizType() {
        return bizType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public String getReqData() {
        return reqData;
    }

    public TranCodeConv getTranCodeConv() {
        return tranCodeConv;
    }

    public Map<String, String> getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader) {
        this.reqHeader = reqHeader;
    }
}
