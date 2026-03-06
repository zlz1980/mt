/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

import com.nantian.nbp.base.model.TranCode;

/**
* 
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class HandleModuleModel extends AstModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 子流程节点集合 */
	private AstList childrenList = null;
	/**  内部交易信息 */
	private TranCode tranCode;
	public AstList getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(AstList childrenList) {
		this.childrenList = childrenList;
	}

	public TranCode getTranCode() {
		return tranCode;
	}

	public void setTranCode(TranCode tranCode) {
		this.tranCode = tranCode;
	}
}
