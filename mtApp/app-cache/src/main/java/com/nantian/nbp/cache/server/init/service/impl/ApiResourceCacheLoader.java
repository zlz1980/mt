package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.Api;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitApiMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Administrator
 */
public class ApiResourceCacheLoader extends ResourceCacheLoader<Api> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResourceCacheLoader.class);

    private final SqlSessionTemplate sqlSessionTemplate;

    public ApiResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<Api> initCache() {
        // 外面使用增强for不需判断null
        List<Api> list = sqlSessionTemplate.getMapper(InitApiMapper.class).findAllApiInfo();
        PbResourceCache<Api> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        for (Api api : list) {
            String apiName = api.getBusiType() + "_" + api.getApiCode() + "_" + api.getJsType();
            resourceCache.put(apiName, api);
            LOGGER.info("apiName [{}] ", apiName);
        }
        return resourceCache;
    }
}
