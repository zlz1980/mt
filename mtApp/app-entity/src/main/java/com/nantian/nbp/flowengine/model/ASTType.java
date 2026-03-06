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
* 逻辑组件
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public enum ASTType {
	/** 普通语法节点 */
	COMM("COMM"),
	/** IF语法节点 */
	IF("IF"),
	/** ELSE语法节点 */
	ELSE("ELSE"),
	/** IFNOT语法节点 */
	IFNOT("IFNOT"),
	/** DOWHILE语法节点 */
	DOWHILE("DOWHILE"),
	/** WHILE语法节点 */
	WHILE("WHILE"),
	/** SWITCH语法节点 */
	SWITCH("SWITCH"),
	/** CASE语法节点，需与SWITCH配合 */
	CASE("CASE"),
	/** BREAK语法节点，需与CASE配合 */
	BREAK("BREAK"),
	/** CASE语法节点，需与SWITCH配合 */
	DEFAULT("DEFAULT"),
	/** RETURN语法节点 */
	RETURN("RETURN"),
	/** EXIT语法节点 */
	EXIT("EXIT"),
	/** HANDLEMODULE处理模块语法节点 */
	HANDLEMODULE("HANDLEMODULE"),
	/** ASYNMODULE处理模块语法节点 */
	ASYNMODULE("ASYNMODULE"),
	/** 临时变量赋值 */
	TEMP("TEMP"),
	/** TRY语法节点 */
	TRY("TRY"),
	/** TRY语法节点 */
	CATCH("CATCH"),
	/** PROC语法节点 */
	PROC("PROC"),
	/** 循环中断 */
	BEK("BEK");

	private final String type;

	ASTType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
