/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

/**
* while 组件
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class WhileModel extends AstModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 循环分支 */
	private AstList loop = null;
	
	public AstList getLoop() {
		return loop;
	}

	public void setLoop(AstList loop) {
		this.loop = loop;
	}

}
