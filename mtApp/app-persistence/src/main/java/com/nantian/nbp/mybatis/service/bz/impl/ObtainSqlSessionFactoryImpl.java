/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.mybatis.service.bz.impl;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.nantian.nbp.mybatis.service.api.bz.ObtainSqlSessionFactory;

/**
* 
* @author pengw at Jul 26, 2020 12:07:37 AM
* @version V2.0
*/
public class ObtainSqlSessionFactoryImpl implements ObtainSqlSessionFactory {

	private SqlSessionFactory sqlSessionFactory;
	private Configuration configuration;


	public ObtainSqlSessionFactoryImpl(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
		this.configuration = sqlSessionFactory.getConfiguration();
	}

	@Override
	public void addMapper(Class<?> type){
		if(!hasMapper(type)){
			configuration.addMapper(type);
		}
	}

	@Override
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	@Override
	public Configuration getConfiguration(){
		return configuration;
	}
	@Override
	public boolean hasMapper(Class<?> type){
		return configuration.hasMapper(type);
	}


	@Override
	public SqlSession openSession(boolean autoCommit) {
		return sqlSessionFactory.openSession(autoCommit);
	}

	@Override
	public void closeSqlSession(SqlSession sqlSession) {
		if(null != sqlSession){
			sqlSession.close();
		}
	}

	@Override
	public <T> T getMapper(SqlSession sqlSession, Class<T> type) {
		return sqlSession.getMapper(type);
	}

}