package com.n.fbsp.atom.platform.seed.other.po;


import java.math.BigDecimal;

/**
 * @Author :
 * @create 2024/2/28 14:18
 */
public class JournalExt {
    public void setDefaultValue() {
        this.tranState = "0";
        this.acctType = "0";
        this.track2 = "0";
        this.track3 = "0";
        this.crdb = "0";
        this.txfFlag = "0";
        this.custFee = new BigDecimal("0.00");
        this.instFee = new BigDecimal("0.00");
        this.currCode = "001";
        this.stlCurrCode = "001";
        this.tlrNo = "0";
        this.branchCode = "0";
        this.reqInstCode = "0";
        this.fwdInstCode = "0";
        this.merTxfDTime = "0";
        this.errCode = "0000";
        this.trmSeqNum = "000000";
        this.trmTranDTime = "0";
        this.trmStlmCode = "0";
        this.trmStlmDTime = "0";
        this.hstRspCode = "0";
        this.hstStlmdTime = "0";
        this.hostStlmCode = "0";
        this.instCode = "0";
        this.instSeqNo = "0";
        this.instTranDTime = "0";
        this.instAcctDate = "0";
        this.instStlmDTime = "0";
        this.instRspCode = "0";
        this.frgnAcctNo = "0";
        this.instStlmCode = "0";
        this.msgType = "0000";
        this.procCode = "000000";
        this.inputMode = "00";
        this.posCode = "00";
        this.schnlNo = "000";
        this.intTxnCode = "000000";
        this.reserve1 = "0";
        this.reserve2 = "0";
        this.reserve3 = "0";
        this.reserve4 = "0";
        this.reserve5 = "0";
        this.reserve6 = "0";
        this.reserve7 = "0";
        this.lastUpDTime = "0";
        this.dac = "00000000";
    }

    /**
     * 请求渠道
     */
    private String reqChnl;
    /**
     * 渠道请求日期
     */
    private String reqDate;
    /**
     * 请求方业务主键
     */
    private String busiKey;
    /**
     * 渠道种类
     */
    private String schnlType;
    /**
     * 系统流水号
     */
    private String sysSeqNum;
    /**
     * 交易状态
     */
    private String tranState;
    /**
     * 业务种类
     */
    private String busiType;
    /**
     * 帐户类型
     */
    private String acctType;
    /**
     * 二磁道信息
     */
    private String track2;
    /**
     * 三磁道信息
     */
    private String track3;
    /**
     * 借贷标志
     */
    private String crdb;
    /**
     * 转帐标志
     */
    private String txfFlag;
    /**
     * 个人手续费
     */
    private BigDecimal custFee;
    /**
     * 机构手续费
     */
    private BigDecimal instFee;
    /**
     * 交易币种代码
     */
    private String currCode;
    /**
     * 清算币种代码
     */
    private String stlCurrCode;
    /**
     * 柜员号
     */
    private String tlrNo;
    /**
     * 受理支行代码
     */
    private String branchCode;
    /**
     * 受理机构号
     */
    private String reqInstCode;
    /**
     * 转发机构号
     */
    private String fwdInstCode;
    /**
     * 商户划帐时间
     */
    private String merTxfDTime;
    /**
     * 错误代码
     */
    private String errCode;
    /**
     * 终端流水号
     */
    private String trmSeqNum;
    /**
     * 终端交易日期时间
     */
    private String trmTranDTime;
    /**
     * 终端对帐标志
     */
    private String trmStlmCode;
    /**
     * 终端对帐日期时间
     */
    private String trmStlmDTime;
    /**
     * 主机应答码
     */
    private String hstRspCode;
    /**
     * 主机对帐时间日期
     */
    private String hstStlmdTime;
    /**
     * 主机对帐标志
     */
    private String hostStlmCode;
    /**
     * 第三方机构代码
     */
    private String instCode;
    /**
     * 机构流水号
     */
    private String instSeqNo;
    /**
     * 机构交易日期时间
     */
    private String instTranDTime;
    /**
     * 机构帐务日期
     */
    private String instAcctDate;
    /**
     * 机构对帐日期时间
     */
    private String instStlmDTime;
    /**
     * 机构应答码
     */
    private String instRspCode;
    /**
     * 客户号
     */
    private String frgnAcctNo;
    /**
     * 机构对帐标志
     */
    private String instStlmCode;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 处理码
     */
    private String procCode;
    /**
     * 输入方式
     */
    private String inputMode;
    /**
     * 条件码
     */
    private String posCode;
    /**
     * 服务渠道号
     */
    private String schnlNo;
    /**
     * 内部交易码
     */
    private String intTxnCode;
    /**
     * 保留字段1
     */
    private String reserve1;
    /**
     * 保留字段2
     */
    private String reserve2;
    /**
     * 保留字段3
     */
    private String reserve3;
    /**
     * 保留字段4
     */
    private String reserve4;
    /**
     * 保留字段5
     */
    private String reserve5;
    /**
     * 保留字段6
     */
    private String reserve6;
    /**
     * 保留字段7
     */
    private String reserve7;
    /**
     * 保留字段8
     */
    private String reserve8;
    /**
     * 保留字段9
     */
    private String reserve9;
    /**
     * 最后修改时间
     */
    private String lastUpDTime;
    /**
     * DAC
     */
    private String dac;
    /**
     * 应用密文
     */
    private String icAppc;
    /**
     * 密文信息数据
     */
    private String icCid;
    /**
     * 发卡行应用数据
     */
    private String icIad;
    /**
     * 不可预知数
     */
    private String icUn;
    /***
     * 应用交易计数器
     */
    private String icAct;
    /**
     * 终端验证结果
     */
    private String icTvr;
    /**
     * 交易日期
     */
    private String icTd;
    /**
     * 交易类型
     */
    private String icTt;
    /**
     * 授权金额
     */
    private String icAa;
    /**
     * 交易货币代码
     */
    private String icTcc;
    /**
     * 应用交互特征
     */
    private String icAip;
    /**
     * 终端国家代码
     */
    private String icTermcc;
    /**
     * 终端国家代码
     */
    private String icAmtoth;
    /**
     * 终端性能
     */
    private String icTerma;
    /**
     * 接口设备序列号
     */
    private String icIfd;
    /**
     * IC卡的序列号
     */
    private String icCsn;
    /**
     * 发卡方脚本结果
     */
    private String icIsr;
    /**
     * 终端读取能力
     */
    private String icTermc;
    /**
     * IC卡条件代码
     */
    private String icConc;
    /**
     * 降级处理标示
     */
    private String icFallback;

    public String getHstStlmdTime() {
        return hstStlmdTime;
    }

    public void setHstStlmdTime(String hstStlmdTime) {
        this.hstStlmdTime = hstStlmdTime;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
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

    public String getBusiKey() {
        return busiKey;
    }

    public void setBusiKey(String busiKey) {
        this.busiKey = busiKey;
    }

    public String getSchnlType() {
        return schnlType;
    }

    public void setSchnlType(String schnlType) {
        this.schnlType = schnlType;
    }

    public String getSysSeqNum() {
        return sysSeqNum;
    }

    public void setSysSeqNum(String sysSeqNum) {
        this.sysSeqNum = sysSeqNum;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getTrack3() {
        return track3;
    }

    public void setTrack3(String track3) {
        this.track3 = track3;
    }

    public String getCrdb() {
        return crdb;
    }

    public void setCrdb(String crdb) {
        this.crdb = crdb;
    }

    public String getStlCurrCode() {
        return stlCurrCode;
    }

    public void setStlCurrCode(String stlCurrCode) {
        this.stlCurrCode = stlCurrCode;
    }

    public String getTlrNo() {
        return tlrNo;
    }

    public void setTlrNo(String tlrNo) {
        this.tlrNo = tlrNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getReqInstCode() {
        return reqInstCode;
    }

    public void setReqInstCode(String reqInstCode) {
        this.reqInstCode = reqInstCode;
    }

    public String getFwdInstCode() {
        return fwdInstCode;
    }

    public void setFwdInstCode(String fwdInstCode) {
        this.fwdInstCode = fwdInstCode;
    }

    public BigDecimal getCustFee() {
        return custFee;
    }

    public void setCustFee(BigDecimal custFee) {
        this.custFee = custFee;
    }

    public BigDecimal getInstFee() {
        return instFee;
    }

    public void setInstFee(BigDecimal instFee) {
        this.instFee = instFee;
    }

    public String getMerTxfDTime() {
        return merTxfDTime;
    }

    public void setMerTxfDTime(String merTxfDTime) {
        this.merTxfDTime = merTxfDTime;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getTrmSeqNum() {
        return trmSeqNum;
    }

    public void setTrmSeqNum(String trmSeqNum) {
        this.trmSeqNum = trmSeqNum;
    }

    public String getTrmTranDTime() {
        return trmTranDTime;
    }

    public void setTrmTranDTime(String trmTranDTime) {
        this.trmTranDTime = trmTranDTime;
    }

    public String getTrmStlmCode() {
        return trmStlmCode;
    }

    public void setTrmStlmCode(String trmStlmCode) {
        this.trmStlmCode = trmStlmCode;
    }

    public String getTrmStlmDTime() {
        return trmStlmDTime;
    }

    public void setTrmStlmDTime(String trmStlmDTime) {
        this.trmStlmDTime = trmStlmDTime;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getInstSeqNo() {
        return instSeqNo;
    }

    public void setInstSeqNo(String instSeqNo) {
        this.instSeqNo = instSeqNo;
    }

    public String getInstTranDTime() {
        return instTranDTime;
    }

    public void setInstTranDTime(String instTranDTime) {
        this.instTranDTime = instTranDTime;
    }

    public String getInstAcctDate() {
        return instAcctDate;
    }

    public void setInstAcctDate(String instAcctDate) {
        this.instAcctDate = instAcctDate;
    }

    public String getInstStlmDTime() {
        return instStlmDTime;
    }

    public void setInstStlmDTime(String instStlmDTime) {
        this.instStlmDTime = instStlmDTime;
    }

    public String getInstRspCode() {
        return instRspCode;
    }

    public void setInstRspCode(String instRspCode) {
        this.instRspCode = instRspCode;
    }

    public String getInstStlmCode() {
        return instStlmCode;
    }

    public void setInstStlmCode(String instStlmCode) {
        this.instStlmCode = instStlmCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getProcCode() {
        return procCode;
    }

    public void setProcCode(String procCode) {
        this.procCode = procCode;
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getSchnlNo() {
        return schnlNo;
    }

    public void setSchnlNo(String schnlNo) {
        this.schnlNo = schnlNo;
    }

    public String getIntTxnCode() {
        return intTxnCode;
    }

    public void setIntTxnCode(String intTxnCode) {
        this.intTxnCode = intTxnCode;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }

    public String getReserve6() {
        return reserve6;
    }

    public void setReserve6(String reserve6) {
        this.reserve6 = reserve6;
    }

    public String getReserve7() {
        return reserve7;
    }

    public void setReserve7(String reserve7) {
        this.reserve7 = reserve7;
    }

    public String getReserve9() {
        return reserve9;
    }

    public void setReserve9(String reserve9) {
        this.reserve9 = reserve9;
    }

    public String getLastUpDTime() {
        return lastUpDTime;
    }

    public void setLastUpDTime(String lastUpDTime) {
        this.lastUpDTime = lastUpDTime;
    }

    public String getDac() {
        return dac;
    }

    public void setDac(String dac) {
        this.dac = dac;
    }

    public String getIcAppc() {
        return icAppc;
    }

    public void setIcAppc(String icAppc) {
        this.icAppc = icAppc;
    }

    public String getIcCid() {
        return icCid;
    }

    public void setIcCid(String icCid) {
        this.icCid = icCid;
    }

    public String getIcIad() {
        return icIad;
    }

    public void setIcIad(String icIad) {
        this.icIad = icIad;
    }

    public String getIcUn() {
        return icUn;
    }

    public void setIcUn(String icUn) {
        this.icUn = icUn;
    }

    public String getIcAct() {
        return icAct;
    }

    public void setIcAct(String icAct) {
        this.icAct = icAct;
    }

    public String getIcTvr() {
        return icTvr;
    }

    public void setIcTvr(String icTvr) {
        this.icTvr = icTvr;
    }

    public String getIcTd() {
        return icTd;
    }

    public void setIcTd(String icTd) {
        this.icTd = icTd;
    }

    public String getIcTt() {
        return icTt;
    }

    public void setIcTt(String icTt) {
        this.icTt = icTt;
    }

    public String getIcAa() {
        return icAa;
    }

    public void setIcAa(String icAa) {
        this.icAa = icAa;
    }

    public String getIcTcc() {
        return icTcc;
    }

    public void setIcTcc(String icTcc) {
        this.icTcc = icTcc;
    }

    public String getIcAip() {
        return icAip;
    }

    public void setIcAip(String icAip) {
        this.icAip = icAip;
    }

    public String getIcTermcc() {
        return icTermcc;
    }

    public void setIcTermcc(String icTermcc) {
        this.icTermcc = icTermcc;
    }

    public String getIcAmtoth() {
        return icAmtoth;
    }

    public void setIcAmtoth(String icAmtoth) {
        this.icAmtoth = icAmtoth;
    }

    public String getIcTerma() {
        return icTerma;
    }

    public void setIcTerma(String icTerma) {
        this.icTerma = icTerma;
    }

    public String getIcIfd() {
        return icIfd;
    }

    public void setIcIfd(String icIfd) {
        this.icIfd = icIfd;
    }

    public String getIcCsn() {
        return icCsn;
    }

    public void setIcCsn(String icCsn) {
        this.icCsn = icCsn;
    }

    public String getIcIsr() {
        return icIsr;
    }

    public void setIcIsr(String icIsr) {
        this.icIsr = icIsr;
    }

    public String getIcTermc() {
        return icTermc;
    }

    public void setIcTermc(String icTermc) {
        this.icTermc = icTermc;
    }

    public String getIcConc() {
        return icConc;
    }

    public void setIcConc(String icConc) {
        this.icConc = icConc;
    }

    public String getIcFallback() {
        return icFallback;
    }

    public void setIcFallback(String icFallback) {
        this.icFallback = icFallback;
    }

    public String getHostStlmCode() {
        return hostStlmCode;
    }

    public void setHostStlmCode(String hostStlmCode) {
        this.hostStlmCode = hostStlmCode;
    }

    public String getReserve8() {
        return reserve8;
    }

    public void setReserve8(String reserve8) {
        this.reserve8 = reserve8;
    }

    public String getTxfFlag() {
        return txfFlag;
    }

    public void setTxfFlag(String txfFlag) {
        this.txfFlag = txfFlag;
    }

    public String getFrgnAcctNo() {
        return frgnAcctNo;
    }

    public void setFrgnAcctNo(String frgnAcctNo) {
        this.frgnAcctNo = frgnAcctNo;
    }

    public String getHstRspCode() {
        return hstRspCode;
    }

    public void setHstRspCode(String hstRspCode) {
        this.hstRspCode = hstRspCode;
    }

    @Override
    public String toString() {
        return "JournalExt{" +
                "reqChnl='" + reqChnl + '\'' +
                ", reqDate='" + reqDate + '\'' +
                ", busiKey='" + busiKey + '\'' +
                ", schnlType='" + schnlType + '\'' +
                ", sysSeqNum='" + sysSeqNum + '\'' +
                ", tranState='" + tranState + '\'' +
                ", busiType='" + busiType + '\'' +
                ", acctType='" + acctType + '\'' +
                ", track2='" + track2 + '\'' +
                ", track3='" + track3 + '\'' +
                ", crdb='" + crdb + '\'' +
                ", txfFlag='" + txfFlag + '\'' +
                ", custFee=" + custFee +
                ", instFee=" + instFee +
                ", currCode='" + currCode + '\'' +
                ", stlCurrCode='" + stlCurrCode + '\'' +
                ", tlrNo='" + tlrNo + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", reqInstCode='" + reqInstCode + '\'' +
                ", fwdInstCode='" + fwdInstCode + '\'' +
                ", merTxfDTime='" + merTxfDTime + '\'' +
                ", errCode='" + errCode + '\'' +
                ", trmSeqNum='" + trmSeqNum + '\'' +
                ", trmTranDTime='" + trmTranDTime + '\'' +
                ", trmStlmCode='" + trmStlmCode + '\'' +
                ", trmStlmDTime='" + trmStlmDTime + '\'' +
                ", hstRspCode='" + hstRspCode + '\'' +
                ", hstStlmdTime='" + hstStlmdTime + '\'' +
                ", hostStlmCode='" + hostStlmCode + '\'' +
                ", instCode='" + instCode + '\'' +
                ", instSeqNo='" + instSeqNo + '\'' +
                ", instTranDTime='" + instTranDTime + '\'' +
                ", instAcctDate='" + instAcctDate + '\'' +
                ", instStlmDTime='" + instStlmDTime + '\'' +
                ", instRspCode='" + instRspCode + '\'' +
                ", frgnAcctNo='" + frgnAcctNo + '\'' +
                ", instStlmCode='" + instStlmCode + '\'' +
                ", msgType='" + msgType + '\'' +
                ", procCode='" + procCode + '\'' +
                ", inputMode='" + inputMode + '\'' +
                ", posCode='" + posCode + '\'' +
                ", schnlNo='" + schnlNo + '\'' +
                ", intTxnCode='" + intTxnCode + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                ", reserve3='" + reserve3 + '\'' +
                ", reserve4='" + reserve4 + '\'' +
                ", reserve5='" + reserve5 + '\'' +
                ", reserve6='" + reserve6 + '\'' +
                ", reserve7='" + reserve7 + '\'' +
                ", reserve8='" + reserve8 + '\'' +
                ", reserve9='" + reserve9 + '\'' +
                ", lastUpDTime='" + lastUpDTime + '\'' +
                ", dac='" + dac + '\'' +
                ", icAppc='" + icAppc + '\'' +
                ", icCid='" + icCid + '\'' +
                ", icIad='" + icIad + '\'' +
                ", icUn='" + icUn + '\'' +
                ", icAct='" + icAct + '\'' +
                ", icTvr='" + icTvr + '\'' +
                ", icTd='" + icTd + '\'' +
                ", icTt='" + icTt + '\'' +
                ", icAa='" + icAa + '\'' +
                ", icTcc='" + icTcc + '\'' +
                ", icAip='" + icAip + '\'' +
                ", icTermcc='" + icTermcc + '\'' +
                ", icAmtoth='" + icAmtoth + '\'' +
                ", icTerma='" + icTerma + '\'' +
                ", icIfd='" + icIfd + '\'' +
                ", icCsn='" + icCsn + '\'' +
                ", icIsr='" + icIsr + '\'' +
                ", icTermc='" + icTermc + '\'' +
                ", icConc='" + icConc + '\'' +
                ", icFallback='" + icFallback + '\'' +
                '}';
    }
}