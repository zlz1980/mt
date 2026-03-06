package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.SysCfg;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitSysCfgMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Administrator
 */
public class SysCfgResourceCacheLoader extends ResourceCacheLoader<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysCfgResourceCacheLoader.class);


    private final SqlSessionTemplate sqlSessionTemplate;

    public SysCfgResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
    @Override
    public PbResourceCache<String> initCache(){
        // 外面使用增强for不需判断null
        List<SysCfg> list = sqlSessionTemplate.getMapper(InitSysCfgMapper.class).findAllSysCfgInfo();
        PbResourceCache<String> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        for (SysCfg sysCfg : list) {
            resourceCache.put(sysCfg.getDefName(), sysCfg.getDefValue());
            LOGGER.info("sysCfgName [{}] ", sysCfg.getDefName());
        }
        return resourceCache;
    }

}
