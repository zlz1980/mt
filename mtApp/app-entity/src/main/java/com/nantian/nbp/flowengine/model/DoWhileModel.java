/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

import java.util.ArrayList;
import java.util.List;

/**
* do-while组件
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
@Deprecated
public class DoWhileModel extends AstModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 条件节点 */
	private AstModel condition = null;
	/** 循环分支 */
	private List<AstModel> loop = null;
	
	public AstModel getCondition() {
		return condition;
	}

	public List<AstModel> getLoop() {
		return loop;
	}

	public void setCondition(AstModel condition) {
		this.condition = condition;
	}

	public void setLoop(List<AstModel> loop) {
		this.loop = loop;
	}

	public void addLoop(AstModel model) {
		if (loop == null) {
			loop = new ArrayList<>();
		}
		loop.add(model);
	}
	
}
