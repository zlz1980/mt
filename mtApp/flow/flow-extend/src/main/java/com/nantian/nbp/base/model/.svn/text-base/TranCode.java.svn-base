/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.base.model;

import java.io.Serializable;
import java.util.Objects;

/**
* 交易码信息(T_PB_TRAN_CODE)
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class TranCode implements Serializable {
	private static final Integer EXTENDCODE_INDEX = 3;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String areaNo;
    private String busiType;
    private String tranCode;
    private String tranName;
    private String tranType;
    private String tranChar;
    private String jourFlag;
    private String tranLevel;
    private int modiTimes;
    private int modiLevel;
    private String tranStatus;
    private String tranExplain;
    private String createtlr;
    private String createDate;
    private String uptlrNo;
    private String updateDate;
    private String note;
    private int owner;
    private int ownLevel;
    private String ownFlag;
    private int leditor;
    private String leTime;
    private String crTime;
    private String extendCode;
	private String apiCode;
	private String isBlocked;
	private FlowKey flowKey;
	private FlowKey parentFlowKey;

	public FlowKey getFlowKey() {
		if(Objects.isNull(flowKey)){
			flowKey = new FlowKey(busiType, tranCode);
		}
		return flowKey;
	}

	public FlowKey getParentFlowKey() {
		if(Objects.isNull(parentFlowKey) && !Objects.isNull(extendCode) && extendCode.length() == 9	){
			String pBusType = extendCode.substring(0, EXTENDCODE_INDEX);
			String pTranCode = extendCode.substring(EXTENDCODE_INDEX);
			parentFlowKey = new FlowKey(pBusType, pTranCode);
		}
		return parentFlowKey;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(String isBlocked) {
		this.isBlocked = isBlocked;
	}

	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
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
	public String getTranName() {
		return tranName;
	}
	public void setTranName(String tranName) {
		this.tranName = tranName;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getTranChar() {
		return tranChar;
	}
	public void setTranChar(String tranChar) {
		this.tranChar = tranChar;
	}
	public String getJourFlag() {
		return jourFlag;
	}
	public void setJourFlag(String jourFlag) {
		this.jourFlag = jourFlag;
	}
	public String getTranLevel() {
		return tranLevel;
	}
	public void setTranLevel(String tranLevel) {
		this.tranLevel = tranLevel;
	}
	public int getModiTimes() {
		return modiTimes;
	}
	public void setModiTimes(int modiTimes) {
		this.modiTimes = modiTimes;
	}
	public int getModiLevel() {
		return modiLevel;
	}
	public void setModiLevel(int modiLevel) {
		this.modiLevel = modiLevel;
	}
	public String getTranStatus() {
		return tranStatus;
	}
	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}
	public String getTranExplain() {
		return tranExplain;
	}
	public void setTranExplain(String tranExplain) {
		this.tranExplain = tranExplain;
	}
	public String getCreatetlr() {
		return createtlr;
	}
	public void setCreatetlr(String createtlr) {
		this.createtlr = createtlr;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUptlrNo() {
		return uptlrNo;
	}
	public void setUptlrNo(String uptlrNo) {
		this.uptlrNo = uptlrNo;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public int getOwnLevel() {
		return ownLevel;
	}
	public void setOwnLevel(int ownLevel) {
		this.ownLevel = ownLevel;
	}
	public String getOwnFlag() {
		return ownFlag;
	}
	public void setOwnFlag(String ownFlag) {
		this.ownFlag = ownFlag;
	}
	public int getLeditor() {
		return leditor;
	}
	public void setLeditor(int leditor) {
		this.leditor = leditor;
	}
	public String getLeTime() {
		return leTime;
	}
	public void setLeTime(String leTime) {
		this.leTime = leTime;
	}
	public String getCrTime() {
		return crTime;
	}
	public void setCrTime(String crTime) {
		this.crTime = crTime;
	}
	public String getExtendCode() {
		return extendCode;
	}
	public void setExtendCode(String extendCode) {
		this.extendCode = extendCode;
	}
}
