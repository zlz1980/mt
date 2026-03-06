package com.n.fbsp.atom.platform.seed.other.po;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 登记簿数据库表对应实体定义
 *
 * @author
 * @since @since 2024-10-28 10:31:03
 */
@Component
public class OriginRequest implements Serializable {

    /**
     * 登记簿主键
     */
    private int id;

    /**
     * 交易日期
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 请求渠道
     */
    private String reqChnl;

    /**
     * 交易码
     */
    private String fTranCode;

    /**
     * 全局流水号
     */
    private String busiKey;

    /**
     * 原始请求报文
     * 数据库字段选用blob，存储二进制信息
     */
    private byte[] reqMessage = null;

    public OriginRequest(String tranDate, String tranTime, String reqChnl, String fTranCode, byte[] reqMessage,
                         String busiKey) {
        this.tranDate = tranDate;
        this.tranTime = tranTime;
        this.reqChnl = reqChnl;
        this.fTranCode = fTranCode;
        this.reqMessage = reqMessage;
        this.busiKey = busiKey;
    }

    public OriginRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getReqChnl() {
        return reqChnl;
    }

    public void setReqChnl(String reqChnl) {
        this.reqChnl = reqChnl;
    }

    public String getfTranCode() {
        return fTranCode;
    }

    public void setfTranCode(String fTranCode) {
        this.fTranCode = fTranCode;
    }

    public byte[] getReqMessage() {
        return reqMessage;
    }

    public void setReqMessage(byte[] reqMessage) {
        this.reqMessage = reqMessage;
    }

    public String getBusiKey() {
        return busiKey;
    }

    public void setBusiKey(String busiKey) {
        this.busiKey = busiKey;
    }

    @Override
    public String toString() {
        return "OriginRequestBo{" + ", traceGid='" + busiKey + '\'' + "tranDate='" + tranDate + '\'' + ", tranTime='" + tranTime + '\'' + ", reqChnl='" + reqChnl + '\'' + ", ftranCode='" + fTranCode + '\'' + ", reqMessage=" + Arrays.toString(reqMessage) + '}';
    }

}
