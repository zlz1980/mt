/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

import java.util.List;

/**
* case 组件
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class CaseModel extends AstModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT = "DEFAULT";
	/** case值 */
	private String value = null;
	/** case分支 */
	private AstList caseList = null;
	/** 中断标识 */
	private boolean isBreak = false;
	
	public String getValue() {
		return value;
	}
	public AstList getCaseList() {
		return caseList;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setCaseList(AstList caseList) {
		this.caseList = caseList;
	}
	public boolean isBreak() {
		return isBreak;
	}
	public void setBreak(boolean isBreak) {
		this.isBreak = isBreak;
	}
}
