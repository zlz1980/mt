/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitAllTranCodeMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TranCodeResourceCacheLoader extends ResourceCacheLoader<TranCode> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TranCodeResourceCacheLoader.class);

    private final SqlSessionTemplate sqlSessionTemplate;

    public TranCodeResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<TranCode> initCache() {
        List<TranCode> list = sqlSessionTemplate.getMapper(InitAllTranCodeMapper.class).findAllTranCode();
        PbResourceCache<TranCode> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        for (TranCode tranCode : list) {
            resourceCache.put(tranCode.getBusiType() + "_" + tranCode.getTranCode(),
                    tranCode);
            LOGGER.info("TranCodeMsg[{}]", tranCode.getTranCode());
        }
        return resourceCache;
    }

}
