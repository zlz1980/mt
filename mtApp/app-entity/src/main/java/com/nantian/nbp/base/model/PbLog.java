/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.base.model;

import com.nantian.nbp.utils.JsonUtil;
import com.nantian.nbp.utils.trim.AutoTrimLength;
import com.nantian.nbp.utils.trim.TrimSupport;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.nantian.nbp.flow.engine.service.api.FeConstants.SUCCESS_TRAN_STATUS;


/**
 * 日志对象
 *
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
@Component
public class PbLog extends TrimSupport implements Serializable {
    /**
     * 交易日期
     */
    private String tranDate = null;
    /**
     * 交易时间
     */
    private String tranTime = null;
    /**
     * 业务Id
     */
    private String bizId = null;
    /**
     * 全局跟踪码
     */
    private String gTrace = null;
    /**
     * 请求渠道
     */
    private String reqChnl = null;
    /**
     * 外部交易码
     */
    private String fTranCode = null;
    /**
     * 业务种类
     */
    private String busiType = null;
    /**
     * 内部流程
     */
    private String tranCode = null;
    /**
     * 流程整体响应时间
     */
    private String pbTime = null;
    /**
     * Log通讯详情
     */
    private String serviceStatus = null;
    /**
     * 流程中请求服务方状态记录
     */
    private List<ServiceStatus> serviceStatusList;
    /**
     * 代理卡内部交易处理状态
     * S-正常,E-异常
     */
    private String tranStatus = SUCCESS_TRAN_STATUS;
    /**
     * 日志路径
     */
    @AutoTrimLength(max = 128)
    private String logPath;
    /**
     * 返回码
     */
    @AutoTrimLength(max = 64)
    private String respCode;
    /**
     * 返回信息
     */
    @AutoTrimLength(max = 256)
    private String respMsg;

    private List<String> stepNoList;

    public void setStepNoList(List<String> stepNoList) {
        this.stepNoList = stepNoList;
    }

    public List<String> getStepNoList() {
        return stepNoList;
    }

    public void addStep(String stepNo) {
        if(Objects.nonNull(stepNoList)){
            stepNoList.add(stepNo);
        }
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        setField("respCode", respCode);
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        setField("respMsg", respMsg);
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        setField("logPath", logPath);
    }

    public String getfTranCode() {
        return fTranCode;
    }

    public void setfTranCode(String fTranCode) {
        this.fTranCode = fTranCode;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public List<ServiceStatus> getServiceStatusList() {
        return serviceStatusList;
    }

    public void setServiceStatusList(List<ServiceStatus> serviceStatusList) {
        this.serviceStatusList = serviceStatusList;
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

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getgTrace() {
        return gTrace;
    }

    public void setgTrace(String gTrace) {
        this.gTrace = gTrace;
    }

    public String getReqChnl() {
        return reqChnl;
    }

    public void setReqChnl(String reqChnl) {
        this.reqChnl = reqChnl;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getPbTime() {
        return pbTime;
    }

    public void setPbTime(String pbTime) {
        this.pbTime = pbTime;
    }

    public void addServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatusList.add(serviceStatus);
        this.serviceStatus = JsonUtil.objToString(this.serviceStatusList);
    }

    @Override
    public String toString() {
        return "PbLog{" +
                "tranDate='" + tranDate + '\'' +
                ", tranTime='" + tranTime + '\'' +
                ", bizId='" + bizId + '\'' +
                ", gTrace='" + gTrace + '\'' +
                ", reqChnl='" + reqChnl + '\'' +
                ", fTranCode='" + fTranCode + '\'' +
                ", busiType='" + busiType + '\'' +
                ", tranCode='" + tranCode + '\'' +
                ", pbTime='" + pbTime + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", serviceStatusList=" + serviceStatusList +
                ", tranStatus='" + tranStatus + '\'' +
                ", logPath='" + logPath + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", stepNoList=" + stepNoList +
                '}';
    }
}
