/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Jul 28, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.base.model;

import java.io.Serializable;

/**
* sql模板(T_PB_EXT_SQL_TEMPLATE)
* @author pengw at Jul 26, 2023 12:07:37 AM
* @version V2.0
*/
public class SqlTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** SQL类型,select/update/delete/insert */
	private String sqlType;
	/** SQL名称，保证唯一 */
	private String sqlName;
	/** SQL脚本 */
	private String sqlScript;
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getSqlScript() {
		return sqlScript;
	}
	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}
	@Override
	public String toString() {
		return "SqlTemplate [sqlName=" + sqlName + ", sqlType=" + sqlType + "]";
	}

}
