/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.SqlTemplate;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitSqlTemplateMapper;
import com.nantian.nbp.cache.server.init.model.SqlTemplateStatement;
import com.nantian.nbp.mybatis.service.api.bz.FbspSqlTemplateService;
import org.apache.ibatis.mapping.MappedStatement;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

public class SqlTemplateResourceCacheLoader extends ResourceCacheLoader<SqlTemplateStatement> {
	private final static Logger LOGGER = LoggerFactory.getLogger(SqlTemplateResourceCacheLoader.class);

	private final SqlSessionTemplate sqlSessionTemplate;

    private final FbspSqlTemplateService sqlTemplateService;

	public SqlTemplateResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate, FbspSqlTemplateService sqlTemplateService) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.sqlTemplateService = sqlTemplateService;
    }

	@Override
	public PbResourceCache<SqlTemplateStatement> initCache() {
        InitSqlTemplateMapper initSqlTemplateMapper = sqlSessionTemplate.getMapper(InitSqlTemplateMapper.class);
		List<SqlTemplate> sqlMapperList = initSqlTemplateMapper.findAllSqlMapper();
		PbResourceCache<SqlTemplateStatement> resourceCache = new PbResourceCache<>(getResourceName(), sqlMapperList.size());
		String sqlName = null;
		String sqlScript = null;
		String sqlType;
		MappedStatement ms;
		try {
			for(SqlTemplate sqlTemplateInfo : sqlMapperList){
				sqlName = sqlTemplateInfo.getSqlName();
				sqlScript = sqlTemplateInfo.getSqlScript();
				sqlType = sqlTemplateInfo.getSqlType();
				ms = sqlTemplateService.buildSqlStatement(sqlName, sqlType, sqlScript);
				if(Objects.isNull(ms)) {
					throw new RuntimeException(APP_CACHE_ERR_KEY + String.format(" buildSqlStatement error! sqlName[%s]",sqlName));
				}
				resourceCache.put(sqlName, new SqlTemplateStatement(sqlName,sqlScript,sqlType,ms));
				LOGGER.info("SqlTemplate cache add sqlName [{}]", sqlName);
			}
		}catch (Exception e){
			LOGGER.error(APP_CACHE_ERR_KEY + " SqlTemplate cache exception sqlName[{}],{}]",sqlName,sqlScript);
			throw e;
		}
		return resourceCache;
	}
}
