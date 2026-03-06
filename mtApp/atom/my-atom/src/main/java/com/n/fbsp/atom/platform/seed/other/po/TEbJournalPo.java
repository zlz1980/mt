package com.n.fbsp.atom.platform.seed.other.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nantian.nbp.utils.trim.AutoTrimLength;
import com.nantian.nbp.utils.trim.TrimSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @Author :
 * @create 2024/2/28 14:18
 */

public class TEbJournalPo extends TrimSupport {
    public final static String REQ_CHNL = "reqChnl";
    public final static String REQ_DATE = "reqDate";
    public final static String REQ_TIME = "reqTime";
    public final static String JOUR_NO = "jourNo";
    public final static String F_TRAN_CODE = "fTranCode";
    public final static String BUSI_KEY = "busiKey";
    public final static String TRAN_DATE = "tranDate";
    public final static String INNER_BUSI_KEY = "innerBusiKey";
    public final static String TRAN_STATE = "tranState";
    public final static String BUSI_STATE = "busiState";
    public final static String CHECK_FLAG = "checkFlag";
    public final static String TAG_SET = "tagSet";
    public final static String EXT_INFO = "extInfo";
    public final static String RESP_CODE = "respCode";
    public final static String MSG_TP = "msgTp";
    public final static String TRANS_PROC_CD = "transProcCd";
    public final static String REF_CODE = "refCode";
    public final static String SVR_RESP_CODE = "svrRespCode";
    public final static String ACCT_NO = "acctNo";
    public final static String AUTH_CODE = "authCode";
    public final static String MER_CODE = "merCode";
    public final static String RLT_REQ_DATE = "rltReqDate";
    public final static String RLT_BUSI_KEY = "rltBusiKey";
    /**
     * 请求渠道
     */
    private String reqChnl;
    /**
     * 渠道请求日期
     */
    private String reqDate;
    /**
     * 渠道请求时间
     */
    private String reqTime;
    /**
     * 平台日期
     */
    private String tranDate;
    /**
     * 平台时间
     */
    private String tranTime;
    /**
     * 平台流水号
     */
    private String jourNo;
    /**
     * 外部交易码
     */
    private String fTranCode;
    /**
     * 请求方业务主键
     */
    private String busiKey;
    /**
     * 业务主键
     */
    private String innerBusiKey;
    /**
     * 原交易请求日期
     */
    private String rltReqDate;
    /**
     * 原交易业务主键
     */
    private String rltBusiKey;
    /**
     * 记账状态I-处理中H-冲正处理中状态E-处理失败状态U-未名状态S-处理成功状态R-已冲正
     */
    private String tranState;
    /**
     * 业务状态C-已撤销D-已完成E-失败S-成功
     */
    private String busiState;
    /**
     * 数据中心渠道类型
     */
    private String busiChnl;
    /**
     * 账务日期
     */
    private String acctDate;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * token账号
     */
    private String tokenAcctNo;
    /**
     * 请求方（原始）交易金额
     */
    private String origTranAmt;
    /**
     * 请求方（原始）交易币种
     */
    private String origCurrCode;
    /**
     * 交易金额
     */
    private BigDecimal tranAmt = new BigDecimal("0.00");
    /**
     * 交易币种代码
     */
    private String currCode;
    /**
     * 交易属性0-正常1-取消2-冲正3-预授权完成7-预授权取消/解冻8-预授权/冻结9-查询
     */
    private String tranAtt;
    /**
     * 对方姓名
     */
    @AutoTrimLength(max = 180)
    private String partnerName;
    /**
     * 对方账号
     */
    private String partnerAcctNo;
    /**
     * 对方行名称
     */
    @AutoTrimLength(max = 180)
    private String partnerBankName;
    /**
     * 对方行机构代码
     */
    private String partnerBankCode;
    /**
     * 应答流水号
     */
    private String respBusiNo;
    /**
     * 应答码
     */
    private String respCode;
    /**
     * 应答信息
     */
    @AutoTrimLength(max = 256)
    private String respDesc;
    /**
     * 详细应答码
     */
    private String subRespCode;
    /**
     * 详细应答码描述
     */
    @AutoTrimLength(max = 256)
    private String subRespDesc;
    /**
     * 服务方报错系统简称
     */
    private String errSvrName;
    /**
     * 服务方报错应答码
     */
    private String svrRespCode;
    /**
     * 服务方报错应答描述
     */
    @AutoTrimLength(max = 256)
    private String svrRespDesc;
    /**
     * 附言
     */
    @AutoTrimLength(max = 256)
    private String posts;
    /**
     * 摘要
     */
    @AutoTrimLength(max = 256)
    private String summary;
    /**
     * 消息类型
     */
    private String msgTp;
    /**
     * 消息处理码
     */
    private String transProcCd;
    /**
     * 受理机构标识码
     */
    private String acptInsIdCd;
    /**
     * 受理机构标识码
     */
    private String fwdInsIdCd;
    /**
     * 服务点条件码
     */
    private String posCondCd;
    /**
     * PAN输入方式码
     */
    private String panEntryMdCd;
    /**
     * 授权码
     */
    private String authCode;
    /**
     * 一级商户编码
     */
    private String merCode;
    /**
     * 一级商户名称
     */
    @AutoTrimLength(max = 256)
    private String merName;
    /**
     * 一级商户类型
     */
    private String merType;
    /**
     * 系统参考号
     */
    private String refCode;
    /**
     * 终端代码
     */
    private String trmCode;
    /**
     * 信用卡核心入账码
     */
    private String accCode;
    /**
     * 信用卡核心入账OR标志
     */
    private String orFlag;
    /**
     * 信用卡核心入账CD方向标志
     */
    private String cdFlag;
    /**
     * 对账标志Y-已对平，N-未对账，E-核对不一致
     */
    private String checkFlag;
    /***
     * 版本信息
     */
    private String version;
    /**
     * 标签特征集
     */
    @JsonIgnore
    private String tagSet;
    /**
     * 扩展信息
     */
    @JsonIgnore
    private String extInfo;
    /**
     * 操作类型
     */
    private String operateType;

    public TEbJournalPo() {
    }

    public String getAcptInsIdCd() {
        return acptInsIdCd;
    }

    public void setAcptInsIdCd(String acptInsIdCd) {
        this.acptInsIdCd = acptInsIdCd;
    }

    public String getFwdInsIdCd() {
        return fwdInsIdCd;
    }

    public void setFwdInsIdCd(String fwdInsIdCd) {
        this.fwdInsIdCd = fwdInsIdCd;
    }

    public String getPosCondCd() {
        return posCondCd;
    }

    public void setPosCondCd(String posCondCd) {
        this.posCondCd = posCondCd;
    }

    public String getPanEntryMdCd() {
        return panEntryMdCd;
    }

    public void setPanEntryMdCd(String panEntryMdCd) {
        this.panEntryMdCd = panEntryMdCd;
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

    public String getBusiChnl() {
        return busiChnl;
    }

    public void setBusiChnl(String busiChnl) {
        this.busiChnl = busiChnl;
    }

    public String getMsgTp() {
        return msgTp;
    }

    public void setMsgTp(String msgTp) {
        this.msgTp = msgTp;
    }

    public String getTransProcCd() {
        return transProcCd;
    }

    public void setTransProcCd(String transProcCd) {
        this.transProcCd = transProcCd;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getOrFlag() {
        return orFlag;
    }

    public void setOrFlag(String orFlag) {
        this.orFlag = orFlag;
    }

    public String getCdFlag() {
        return cdFlag;
    }

    public void setCdFlag(String cdFlag) {
        this.cdFlag = cdFlag;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public String getBusiState() {
        return busiState;
    }

    public void setBusiState(String busiState) {
        this.busiState = busiState;
    }

    public String getOrigCurrCode() {
        return origCurrCode;
    }

    public void setOrigCurrCode(String origCurrCode) {
        this.origCurrCode = origCurrCode;
    }

    public BigDecimal getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(BigDecimal tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getReqChnl() {
        return reqChnl;
    }

    public void setReqChnl(String reqChnl) {
        this.reqChnl = reqChnl;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getJourNo() {
        return jourNo;
    }

    public void setJourNo(String jourNo) {
        this.jourNo = jourNo;
    }

    public String getfTranCode() {
        return fTranCode;
    }

    public void setfTranCode(String fTranCode) {
        this.fTranCode = fTranCode;
    }

    public String getBusiKey() {
        return busiKey;
    }

    public void setBusiKey(String busiKey) {
        this.busiKey = busiKey;
    }

    public String getInnerBusiKey() {
        return innerBusiKey;
    }

    public void setInnerBusiKey(String innerBusiKey) {
        this.innerBusiKey = innerBusiKey;
    }

    public String getRltReqDate() {
        return rltReqDate;
    }

    public void setRltReqDate(String rltReqDate) {
        this.rltReqDate = rltReqDate;
    }

    public String getRltBusiKey() {
        return rltBusiKey;
    }

    public void setRltBusiKey(String rltBusiKey) {
        this.rltBusiKey = rltBusiKey;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public String getAcctDate() {
        return acctDate;
    }

    public void setAcctDate(String acctDate) {
        this.acctDate = acctDate;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getTokenAcctNo() {
        return tokenAcctNo;
    }

    public void setTokenAcctNo(String tokenAcctNo) {
        this.tokenAcctNo = tokenAcctNo;
    }

    public String getOrigTranAmt() {
        return origTranAmt;
    }

    public void setOrigTranAmt(String origTranAmt) {
        this.origTranAmt = origTranAmt;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getTranAtt() {
        return tranAtt;
    }

    public void setTranAtt(String tranAtt) {
        this.tranAtt = tranAtt;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        setField("partnerName", partnerName);
    }

    public String getPartnerAcctNo() {
        return partnerAcctNo;
    }

    public void setPartnerAcctNo(String partnerAcctNo) {
        setField("partnerAcctNo", partnerAcctNo);
    }

    public String getPartnerBankName() {
        return partnerBankName;
    }

    public void setPartnerBankName(String partnerBankName) {
        setField("partnerBankName", partnerBankName);
    }

    public String getPartnerBankCode() {
        return partnerBankCode;
    }

    public void setPartnerBankCode(String partnerBankCode) {
        this.partnerBankCode = partnerBankCode;
    }

    public String getRespBusiNo() {
        return respBusiNo;
    }

    public void setRespBusiNo(String respBusiNo) {
        this.respBusiNo = respBusiNo;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        setField("respDesc", respDesc);
    }

    public String getSubRespCode() {
        return subRespCode;
    }

    public void setSubRespCode(String subRespCode) {
        this.subRespCode = subRespCode;
    }

    public String getSubRespDesc() {
        return subRespDesc;
    }

    public void setSubRespDesc(String subRespDesc) {
        setField("subRespDesc", subRespDesc);
    }

    public String getErrSvrName() {
        return errSvrName;
    }

    public void setErrSvrName(String errSvrName) {
        this.errSvrName = errSvrName;
    }

    public String getSvrRespCode() {
        return svrRespCode;
    }

    public void setSvrRespCode(String svrRespCode) {
        this.svrRespCode = svrRespCode;
    }

    public String getSvrRespDesc() {
        return svrRespDesc;
    }

    public void setSvrRespDesc(String svrRespDesc) {
        setField("svrRespDesc", svrRespDesc);
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        setField("posts", posts);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        setField("summary", summary);
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        setField("merName", merName);
    }

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getTrmCode() {
        return trmCode;
    }

    public void setTrmCode(String trmCode) {
        this.trmCode = trmCode;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTagSet() {
        return tagSet;
    }

    public void setTagSet(String tagSet) {
        this.tagSet = tagSet;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public void setUpdateTagSet(Set<String> tagSets) {
        this.setTagSet(StringUtils.arrayToCommaDelimitedString(tagSets.toArray()));
    }


}
