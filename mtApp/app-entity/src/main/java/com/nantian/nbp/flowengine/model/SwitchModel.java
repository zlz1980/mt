/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

import java.util.LinkedHashMap;
import java.util.List;

/**
* Switch 组件
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class SwitchModel extends AstModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** case分支 */
	private LinkedHashMap<String,CaseModel> caseBranch = null;

	public LinkedHashMap<String, CaseModel> getCaseBranch() {
		return caseBranch;
	}

	public void setCaseBranch(LinkedHashMap<String, CaseModel> caseBranch) {
		this.caseBranch = caseBranch;
	}
}
