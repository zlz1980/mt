package org.jts.pkg;

import static org.jts.pkg.PkgHttpHeadKeys.PKG_JSON_VERSION;

public class PkgResHead {
    // 机构号,0000为无机构标识
    private String org;
    // 柜员号,6位员工号或虚拟柜员号，非使用员工号场景可登记用户ID填写
    private String id;
    // 前台流水号,基于数据标准制定的企业全局唯一的交易序号，用于标识企业内唯一笔交易。目前同集中交换体系12位前台流水号。
    private String requestNo;
    // 后台流水号,基于数据标准制定的系统全局唯一的交易序号，用于标志系统内为一笔交易。
    private String responseNo;
    // 渠道号,继承自集中交换体系的渠道号属性
    private String channelNo;
    // 交易日期,请求发生的自然日期，服务方返回报文中应原样返回该属性值，和前台流水号组合形成交易唯一索引。
    private String date;
    // 交易时间,请求发生的自然日期，服务方返回报文中应原样返回该属性值，和前台流水号组合形成交易唯一索引。
    private String time;
    // 交易处理响应码,同报文头ceb-common-returncode字段
    private String returnCode;
    /*
    * 报文类型,normal：标准业务报文体；error：错误业务报文体；少数交易在业务异常后，
    * 需要标准业务报文体返回业务错误信息，如数组或列表情况。大部分场景下，
    * 常规业务报文为normal，异常报文为error,部分场景下需同时返回异常报文和常规业务报文为errnor。
    * */
    private String messageType;
    // 报文头规范版本,同HTTP报文头内定义
    private String version = PKG_JSON_VERSION;
    //业务领域标识,coreBank,sop
    private String domain;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getResponseNo() {
        return responseNo;
    }

    public void setResponseNo(String responseNo) {
        this.responseNo = responseNo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
