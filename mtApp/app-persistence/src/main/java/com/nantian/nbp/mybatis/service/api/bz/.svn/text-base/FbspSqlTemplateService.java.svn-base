/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:liulei at Dec 20, 2023 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.api.bz;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

/**
 * 自定义mybatis sql模板解析类
 * @author liulei
 *
 */
public interface FbspSqlTemplateService {

	/**
	 * 解析预定于的sql模板
	 * @param sqlName 模板名称,唯一,用于生成mybatis xml的sqlId
	 * @param sqlType 模板类型，insert、update、delete、select,根据类型拼接模板
	 * @param sqlScript sql模板内容
	 * @return MappedStatement
	 */
	MappedStatement buildSqlStatement(String sqlName, String sqlType, String sqlScript);

	/**
	 * 获取Mybatis配置类
	 * @return Configuration
	 */
	Configuration getConfiguration() ;
	
}
