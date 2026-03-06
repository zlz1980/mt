/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.base.model;

/**
* 渠道与外部交易码对应关系
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class FTranKey {

	/** 渠道号 */
	private String chnlNo;
	/** 外部交易码 */
	private String fTranCode;
	
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
	public FTranKey() {
		super();
	}
	public FTranKey(String chnlNo, String fTranCode) {
		super();
		this.chnlNo = chnlNo;
		this.fTranCode = fTranCode;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chnlNo == null) ? 0 : chnlNo.hashCode());
		result = prime * result
				+ ((fTranCode == null) ? 0 : fTranCode.hashCode());
		return result;		
	}
	
	
	@Override
	public boolean equals(Object obj){
		if (this==obj) {
            return true;
        }
		if (obj==null) {
            return false;
        }
		if (getClass()!=obj.getClass()) {
            return false;
        }
		FTranKey other=(FTranKey) obj;
		if (chnlNo==null){
			if (other.getChnlNo()!=null) {
                return false;
            }
		}		
		else if (!chnlNo.equals(other.getChnlNo())) {
            return false;
        }
		if (fTranCode==null){
			if (other.getfTranCode()!=null) {
                return false;
            }
		}else if (!fTranCode.equals(other.getfTranCode())) {
            return false;
        }
		return true;		
	}
	
	@Override
	public String toString(){
		return chnlNo+"_"+fTranCode;
		
	}

}
