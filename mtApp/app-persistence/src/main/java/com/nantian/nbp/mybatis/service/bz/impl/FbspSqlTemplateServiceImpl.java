/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Dec 20, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.bz.impl;

import com.nantian.nbp.mybatis.service.api.bz.FbspSqlTemplateService;
import com.nantian.nbp.mybatis.service.builder.FbspMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FbspSqlTemplateServiceImpl implements FbspSqlTemplateService{
	private final static Logger LOGGER = LoggerFactory.getLogger(FbspSqlTemplateServiceImpl.class);

	private static final String XML_FILE_TYPE = ".mapper.xml";
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"gbk\"?> \t<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\t";
	private static final String MAPPER_START = "<mapper namespace=\"com.cebbank.ext.sql.template\">\t";
	private static final String MAPPER_T_END = "</mapper>\t";

	private final Configuration configuration;
	
	public FbspSqlTemplateServiceImpl(Configuration configuration){
		this.configuration = configuration;
	}

	@Override
	public MappedStatement buildSqlStatement(String sqlName, String sqlType, String sqlScript) {
		return mapperBuild(sqlName,sqlType,sqlScript);
	}

	/**
	 * 根据类型拼接sql mapper
	 * @param sqlName
	 * @param sqlType
	 * @param script
	 * @return
	 */
	private MappedStatement mapperBuild(String sqlName,String sqlType, String script) {
		String lowSqlType = sqlType.toLowerCase();
		String statementXml = "";
		switch (lowSqlType) {
        case "insert":
			statementXml = buildInsertXml(sqlName,script);
			break;
		case "update": 
			statementXml = buildUpdateXml(sqlName,script);
			break;
		case "delete": 
			statementXml = buildDeleteXml(sqlName,script);
			break;
		default:
			statementXml = buildSelectXml(sqlName,script);
			break;
		}
		String resource = buildResourceStr(sqlName);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("sqlResource: [{}],sqlMapper: [{}]",resource,statementXml);
		}
		FbspMapperBuilder mapperParser = new FbspMapperBuilder(statementXml, configuration,
				resource, configuration.getSqlFragments());
		return mapperParser.parse();
	}
	
	/**
	 * 拼接查询语句mapper
	 * @param sqlId
	 * @param script
	 * @return
	 */
	private String buildSelectXml(String sqlId,String script) {
		String statementStart = "<select id=\"" + sqlId + "\" parameterType=\"hashmap\" resultType=\"hashmap\">\t";
		String statementEnd = "</select>\t";
		return XML_HEADER + MAPPER_START + statementStart + script + statementEnd + MAPPER_T_END;
	}
	
	/**
	 * 拼接新增语句mapper
	 * @param sqlId
	 * @param script
	 * @return
	 */
	private String buildInsertXml(String sqlId,String script) {
		String statementStart = "<insert id=\"" + sqlId + "\" parameterType=\"hashmap\" >\t";
		String statementEnd = "</insert>\t";
		return XML_HEADER + MAPPER_START + statementStart + script + statementEnd + MAPPER_T_END;
	}
	
	/**
	 * 拼接修改语句mapper
	 * @param sqlId
	 * @param script
	 * @return
	 */
	private String buildUpdateXml(String sqlId,String script) {
		String statementStart = "<update id=\"" + sqlId + "\" parameterType=\"hashmap\" >\t";
		String statementEnd = "</update>\t";
		return XML_HEADER + MAPPER_START + statementStart + script + statementEnd + MAPPER_T_END;
	}
	
	/**
	 * 拼接删除语句mapper
	 * @param sqlId
	 * @param script
	 * @return
	 */
	private String buildDeleteXml(String sqlId,String script) {
		String statementStart = "<delete id=\"" + sqlId + "\" parameterType=\"hashmap\" >\t";
		String statementEnd = "</delete>\t";
		return XML_HEADER + MAPPER_START + statementStart + script + statementEnd + MAPPER_T_END;
	}
	
	private String buildResourceStr(String sqlId){
		return sqlId+ XML_FILE_TYPE;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

}
