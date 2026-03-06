/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.cache.server.ResourceCacheLoader;
import com.nantian.nbp.cache.server.cache.PbResourceCache;
import com.nantian.nbp.cache.server.init.mapper.InitTranCodeConvMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class TranCodeConvResourceCacheLoader extends ResourceCacheLoader<TranCodeConv> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranCodeConvResourceCacheLoader.class);

    private final SqlSessionTemplate sqlSessionTemplate;

    public TranCodeConvResourceCacheLoader(String resourceName, SqlSessionTemplate sqlSessionTemplate) {
        super(resourceName);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public PbResourceCache<TranCodeConv> initCache() {
        List<TranCodeConv> list = sqlSessionTemplate.getMapper(InitTranCodeConvMapper.class).findTranCodeConvList();
        PbResourceCache<TranCodeConv> resourceCache = new PbResourceCache<>(getResourceName(),list.size());
        for (TranCodeConv tranCodeConv : list) {
            resourceCache.put( tranCodeConv.getChnlNo() + "_" + tranCodeConv.getfTranCode()
                    , tranCodeConv);
            LOGGER.info("FTranKey [{}]_[{}] ", tranCodeConv.getChnlNo(), tranCodeConv.getTranCode());
        }
        return resourceCache;
    }

}
