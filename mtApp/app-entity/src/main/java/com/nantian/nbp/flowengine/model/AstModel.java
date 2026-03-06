/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.flowengine.model;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.AtomService;

import java.io.Serializable;
import java.util.Objects;

/**
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class AstModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 节点语法类型 */
	private ASTType type = null;
	/** 流程节点信息 */
	private FlowUnit unit = null;
	/** */
	private AtomService atomService = null;
	/**
	 * 模块结束步骤号
	 */
	private int endStep;
	
	public FlowUnit getUnit() {
		return unit;
	}
	
	public void setUnit(FlowUnit unit) {
		this.unit = unit;
	}

	public ASTType getType() {
		return type;
	}

	public void setType(ASTType type) {
		this.type = type;
	}

	public String getAtomTranParam(){
		if(Objects.isNull(unit)){
			return null;
		}
		return unit.getAtomTranParam();
	}

	public AtomService getAtomService() {
		return atomService;
	}

	public void setAtomService(AtomService atomService) {
		this.atomService = atomService;
	}

	public int getEndStep() {
		return endStep;
	}

	public void setEndStep(int endStep) {
		this.endStep = endStep;
	}
/*
	public List<String> getParamList(){
		if(Objects.isNull(unit)){
			return null;
		}
		return unit.getParamList();
	}

	 */
}
