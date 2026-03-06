package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.HttpConf;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitHttpConfMapper;
import com.nantian.nbp.flow.engine.service.api.HttpSvcClientTemplate;
import com.nantian.nbp.utils.SpringContextUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.nantian.nbp.cache.server.api.Constants.APP_CACHE_ERR_KEY;

/**
 * @author Administrator
 */
public class HttpConfResourceCacheLoader extends ResourceCacheLoader<HttpConf> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConfResourceCacheLoader.class);

    protected final SqlSessionTemplate sqlSessionTemplate;

    public HttpConfResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<HttpConf> initCache(){
        List<HttpConf> list = sqlSessionTemplate.getMapper(InitHttpConfMapper.class).findAllHttpConfList();
        String serName = null;
        String httpBeanName = null;
        PbResourceCache<HttpConf> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        try{
            for (HttpConf conf : list){
                /* 基于配置信息初始化对应集合属性 */
                conf.initRetCodesSet();
                serName = conf.getSerName();
                httpBeanName = conf.getBeanName();
                conf.setClientTemplate(SpringContextUtils.getBean(httpBeanName, HttpSvcClientTemplate.class));
                resourceCache.put(serName,conf);
                LOGGER.info("HttpConf cache add serName [{}]", serName);
            }
            return resourceCache;
        } catch (Exception e) {
            throw new RuntimeException(String.format(APP_CACHE_ERR_KEY + " Http[%s] bean Util[%s] is not support",serName,httpBeanName),e);
        }
    }

}
