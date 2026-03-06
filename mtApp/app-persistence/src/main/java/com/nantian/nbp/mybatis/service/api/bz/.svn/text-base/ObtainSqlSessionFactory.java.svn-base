/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.api.bz;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
* 自定义mybatis mapper扫描注入
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public interface ObtainSqlSessionFactory {
	SqlSessionFactory getSqlSessionFactory();

	Configuration getConfiguration();

	/**
	 * 判断mybatis mapper 是否存在
	 * @param paramClass
	 * @return
	 */
    boolean hasMapper(Class<?> paramClass);

	/**
	 * mapper 新增
	 * @param paramClass
	 */
    void addMapper(Class<?> paramClass);

	
	SqlSession openSession(boolean paramBoolean);

	void closeSqlSession(SqlSession paramSqlSession);

	<T> T getMapper(SqlSession paramSqlSession,
                    Class<T> paramClass);
}
