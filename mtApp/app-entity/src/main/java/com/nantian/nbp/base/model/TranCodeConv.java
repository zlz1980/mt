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

/**
* 渠道、外部交易码、业务种类、内部交易码对应关系
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class TranCodeConv implements Serializable{
	public static final String TURNOVER_FLAG_ON = "1";

	private static final long serialVersionUID = 1L;
	/** 区域号 */
	private String areaNo;
	/** 渠道号 */
	private String chnlNo;
	/** 外部交易码 */
	private String fTranCode;
	/** 业务种类 */
	private String busiType;
	/** 内部交易码 */
	private String tranCode;
	/** 日志级别 */
	private String logLevel;

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getChnlNo() {
		return chnlNo;
	}

	public void setChnlNo(String chnlNo) {
		this.chnlNo = chnlNo;
	}

	public String getfTranCode() {
		return fTranCode;
	}

	public void setfTranCode(String fTranCode) {
		this.fTranCode = fTranCode;
	}

	public String getBusiType() {
		return busiType;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public FTranKey createFTranKey() {
		return new FTranKey(chnlNo, fTranCode);
	}

	public FlowKey createFlowKey() {
		return new FlowKey(busiType, tranCode);
	}

	@Override
	public String toString() {
		return "TranCodeConv{" +
				"areaNo='" + areaNo + '\'' +
				", chnlNo='" + chnlNo + '\'' +
				", fTranCode='" + fTranCode + '\'' +
				", busiType='" + busiType + '\'' +
				", tranCode='" + tranCode + '\'' +
				", logLevel='" + logLevel + '\'' +
				'}';
	}


}
