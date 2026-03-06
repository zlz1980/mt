/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.model;

import java.io.Serializable;

import org.apache.ibatis.mapping.MappedStatement;

public class SqlTemplateStatement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sqlName;
	private String sqlSource;
	private String sqlType;
	private MappedStatement ms;
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getSqlSource() {
		return sqlSource;
	}
	public void setSqlSource(String sqlSource) {
		this.sqlSource = sqlSource;
	}
	public MappedStatement getMs() {
		return ms;
	}
	public void setMs(MappedStatement ms) {
		this.ms = ms;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public SqlTemplateStatement() {
		super();
	}
	public SqlTemplateStatement(String sqlName, String sqlSource, String sqlType, MappedStatement ms) {
		super();
		this.sqlName = sqlName;
		this.sqlSource = sqlSource;
		this.sqlType = sqlType;
		this.ms = ms;
	}
	
	
}
