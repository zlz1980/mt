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
* 流程增强处理参数表（T_PB_EXT_SAGA_PARA）
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
/**
 * @author w46838
 *
 */
public class SagaPara implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 区域号 */
    private String areaNo;
    /** 业务种类 */
    private String busiType;
    /** 内部交易码 */
    private String tranCode;
    /** 原子交易序号 */
    private int atomTranNo;
    /** 原子交易码 */
    private String atomTranCode;
    /** 返回值 */
    private int retValue;
    /** 原子交易参数 */
    private String atomPara;
    /** 备注 */
    private String note;
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
	public int getAtomTranNo() {
		return atomTranNo;
	}
	public void setAtomTranNo(int atomTranNo) {
		this.atomTranNo = atomTranNo;
	}
	public String getAtomTranCode() {
		return atomTranCode;
	}
	public void setAtomTranCode(String atomTranCode) {
		this.atomTranCode = atomTranCode;
	}
	public int getRetValue() {
		return retValue;
	}
	public void setRetValue(int retValue) {
		this.retValue = retValue;
	}
	public String getAtomPara() {
		return atomPara;
	}
	public void setAtomPara(String atomPara) {
		this.atomPara = atomPara;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
}
