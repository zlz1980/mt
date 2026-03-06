package org.jts.cache.config;

import org.jts.cache.CacheClientApi;
import org.jts.cache.CacheClientApiImpl;
import org.jts.cache.loader.CacheLoader;
import org.jts.cache.loader.CacheLoaderManager;
import org.jts.cache.resource.impl.NewCodeCacheLoader;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PbLocalCacheConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbLocalCacheConfig.class);

    @Value("${cacheData.initCacheTimeout:180}")
    private long initCacheTimeout;

    private void registerBaseResLoaders(CacheLoader cacheLoader,SqlSessionTemplate sqlSessionTemplate) {
        cacheLoader.registerBaseResourceCacheLoader(new NewCodeCacheLoader(sqlSessionTemplate));

    }

    @Bean
    public CacheLoader cacheLoader(SqlSessionTemplate sqlSessionTemplate) {
        CacheLoaderManager cacheService = new CacheLoaderManager(initCacheTimeout);
        registerBaseResLoaders(cacheService,sqlSessionTemplate);
        return cacheService;
    }

    @Bean
    public CacheClientApi cacheClientApi( CacheLoader cacheLoader) {
        return new CacheClientApiImpl(cacheLoader::getMainPbCache);
    }


}