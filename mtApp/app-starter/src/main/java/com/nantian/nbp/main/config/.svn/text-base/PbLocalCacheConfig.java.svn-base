package com.nantian.nbp.main.config;

import com.nantian.nbp.cache.server.CacheLoader;
import com.nantian.nbp.cache.server.CacheLoaderManager;
import com.nantian.nbp.cache.server.api.CacheClientApi;
import com.nantian.nbp.cache.server.impl.CacheClientApiImpl;
import com.nantian.nbp.cache.server.init.service.impl.ApiResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.CacheTableResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.DecisionResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.FbspFlowExtendServiceImpl;
import com.nantian.nbp.cache.server.init.service.impl.FlowContainerResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.HttpConfResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.SqlTemplateResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.SysCfgResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.TranCodeConvResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.TranCodeResourceCacheLoader;
import com.nantian.nbp.cache.server.init.service.impl.UtilsBeanResourceCacheLoader;
import com.nantian.nbp.mybatis.service.api.bz.FbspSqlTemplateService;
import com.nantian.nbp.rule.cache.server.init.service.impl.RuleBizResourceCacheLoader;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.nantian.nbp.cache.server.api.Constants.API_KEY;
import static com.nantian.nbp.cache.server.api.Constants.DECISION_TBL_PRE;
import static com.nantian.nbp.cache.server.api.Constants.FLOW_CONTAINER;
import static com.nantian.nbp.cache.server.api.Constants.HTTP_CONF_KEY;
import static com.nantian.nbp.cache.server.api.Constants.RULE_BIZ_TYPE_KEY;
import static com.nantian.nbp.cache.server.api.Constants.SQL_TEMPLATE_KEY;
import static com.nantian.nbp.cache.server.api.Constants.STATIC_TBL_PRE;
import static com.nantian.nbp.cache.server.api.Constants.SYS_CFG_KEY;
import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_CONV_KEY;
import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_MSG_KEY;
import static com.nantian.nbp.cache.server.api.Constants.UTILS_BEAN_KEY;
import static com.nantian.nbp.utils.StrUtils.EMPTY_STR;

/**
 * Created by leo on 2019/7/16.
 */
@Configuration
public class PbLocalCacheConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PbLocalCacheConfig.class);

    @Value("${cacheData.initCacheTimeout:180}")
    private long initCacheTimeout;

    private void registerBaseResLoaders(CacheLoader cacheLoader,
            SqlSessionTemplate sqlSessionTemplate, FbspSqlTemplateService fbspSqlTemplateService) {
        cacheLoader.registerBaseResourceCacheLoader(new RuleBizResourceCacheLoader(RULE_BIZ_TYPE_KEY, sqlSessionTemplate, EMPTY_STR));
        cacheLoader.registerBaseResourceCacheLoader(new TranCodeConvResourceCacheLoader(TRAN_CODE_CONV_KEY, sqlSessionTemplate));
        cacheLoader.registerBaseResourceCacheLoader(new CacheTableResourceCacheLoader(STATIC_TBL_PRE, sqlSessionTemplate));
        cacheLoader.registerBaseResourceCacheLoader(new HttpConfResourceCacheLoader(HTTP_CONF_KEY, sqlSessionTemplate));
        cacheLoader.registerBaseResourceCacheLoader(new SqlTemplateResourceCacheLoader(SQL_TEMPLATE_KEY, sqlSessionTemplate, fbspSqlTemplateService));
        cacheLoader.registerBaseResourceCacheLoader(new SysCfgResourceCacheLoader(SYS_CFG_KEY, sqlSessionTemplate));
        cacheLoader.registerBaseResourceCacheLoader(new ApiResourceCacheLoader(API_KEY, sqlSessionTemplate));
        cacheLoader.registerBaseResourceCacheLoader(new UtilsBeanResourceCacheLoader(UTILS_BEAN_KEY, sqlSessionTemplate));
    }

    private void registerExtResLoaders(CacheLoader cacheLoader,
            SqlSessionTemplate sqlSessionTemplate) {
        cacheLoader.registerExtResourceCacheLoader(new TranCodeResourceCacheLoader(TRAN_CODE_MSG_KEY, sqlSessionTemplate));
        FbspFlowExtendServiceImpl flowExtendService = new FbspFlowExtendServiceImpl(cacheLoader::getFlushingPbCache);
        cacheLoader.registerExtResourceCacheLoader(new FlowContainerResourceCacheLoader(FLOW_CONTAINER, sqlSessionTemplate, flowExtendService));
        cacheLoader.registerExtResourceCacheLoader(new DecisionResourceCacheLoader(DECISION_TBL_PRE, sqlSessionTemplate, cacheLoader::getFlushingPbCache));
    }


    @Bean(name = "cacheLoaderManager")
    public CacheLoader cacheLoaderManager(FbspSqlTemplateService fbspSqlTemplateService,
            SqlSessionTemplate sqlSessionTemplate) {
        CacheLoaderManager cacheService = new CacheLoaderManager(sqlSessionTemplate, initCacheTimeout);
        registerBaseResLoaders(cacheService, sqlSessionTemplate, fbspSqlTemplateService);
        registerExtResLoaders(cacheService, sqlSessionTemplate);
        return cacheService;
    }

    @Bean(name = "cacheClientApi")
    public CacheClientApi getCacheClientApi(@Qualifier("cacheLoaderManager") CacheLoader cacheLoader) {
        return new CacheClientApiImpl(cacheLoader::getMainPbCache);
    }

}