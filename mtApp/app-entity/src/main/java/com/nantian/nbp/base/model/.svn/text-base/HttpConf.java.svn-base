package com.nantian.nbp.base.model;

import com.nantian.nbp.flow.engine.service.api.HttpSvcClientTemplate;
import com.nantian.nbp.utils.StrUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class HttpConf implements Serializable {

    /** 服务方系统简称 */
    private String sysAbbr;
    /** 服务名 */
    private String serName;
    /** 服务处理bean */
    private String beanName;
    /** 映射URL地址 */
    private String gatewayUrl;
    /** 返回码名称或路径 */
    private String retCodeFieldName;
    /** 错误返回信息名称或路径 */
    private String retErrMsgFieldName;
    /** 错误返回码名称或路径 */
    private String retErrCodeFieldName;
    /** 系统日期名称或路径 */
    private String reqDateFieldName;
    /** 请求主键名称或路径 */
    private String innerBusikeyFieldName;
    /** 未明返回码串 */
    private String unknownRetCodeSet;
    /** 成功返回码串 */
    private String successRetCodeSet;
    /** 未明返回码集合 */
    private final Set<String> unknownRetCodes = new HashSet<>();
    /** 成功返回码集合 */
    private final Set<String> successRetCodes = new HashSet<>();
    /** HTTP客户端模板 */
    private HttpSvcClientTemplate clientTemplate;

    public String getRetErrCodeFieldName() {
        return retErrCodeFieldName;
    }

    public void setRetErrCodeFieldName(String retErrCodeFieldName) {
        this.retErrCodeFieldName = retErrCodeFieldName;
    }

    public String getReqDateFieldName() {
        return reqDateFieldName;
    }

    public void setReqDateFieldName(String reqDateFieldName) {
        this.reqDateFieldName = reqDateFieldName;
    }

    public String getInnerBusikeyFieldName() {
        return innerBusikeyFieldName;
    }

    public void setInnerBusikeyFieldName(String innerBusikeyFieldName) {
        this.innerBusikeyFieldName = innerBusikeyFieldName;
    }

    public String getSysAbbr() {
        return sysAbbr;
    }

    public void setSysAbbr(String sysAbbr) {
        this.sysAbbr = sysAbbr;
    }

    public String getRetErrMsgFieldName() {
        return retErrMsgFieldName;
    }

    public void setRetErrMsgFieldName(String retErrMsgFieldName) {
        this.retErrMsgFieldName = retErrMsgFieldName;
    }

    public String getSerName() {
        return serName;
    }

    public void setSerName(String serName) {
        this.serName = serName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getRetCodeFieldName() {
        return retCodeFieldName;
    }

    public void setRetCodeFieldName(String retCodeFieldName) {
        this.retCodeFieldName = retCodeFieldName;
    }

    public String getUnknownRetCodeSet() {
        return unknownRetCodeSet;
    }

    public void setUnknownRetCodeSet(String unknownRetCodeSet) {
        this.unknownRetCodeSet = unknownRetCodeSet;
    }

    public String getSuccessRetCodeSet() {
        return successRetCodeSet;
    }

    public void setSuccessRetCodeSet(String successRetCodeSet) {
        this.successRetCodeSet = successRetCodeSet;
    }

    public Set<String> getUnknownRetCodes() {
        return unknownRetCodes;
    }

    public Set<String> getSuccessRetCodes() {
        return successRetCodes;
    }

    /* 基于配置信息初始化对应集合属性 */
    public void initRetCodesSet() {
        if(StrUtils.hasText(unknownRetCodeSet)) {
            unknownRetCodes.addAll(StrUtils.commaDelimitedListToSet(unknownRetCodeSet));
        }
        if(StrUtils.hasText(successRetCodeSet)) {
            successRetCodes.addAll(StrUtils.commaDelimitedListToSet(successRetCodeSet));
        }
    }

    public HttpSvcClientTemplate getClientTemplate() {
        return clientTemplate;
    }

    public void setClientTemplate(HttpSvcClientTemplate clientTemplate) {
        this.clientTemplate = clientTemplate;
    }

    @Override
    public String toString() {
        return serName;
    }
}
