/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.cache.server;


import com.nantian.nbp.cache.server.cache.PbCache;
import com.nantian.nbp.cache.server.model.RefushResult;

public interface CacheLoader {

    PbCache getMainPbCache();

    PbCache getFlushingPbCache();

    void initAll(String serverName);

    RefushResult reloadAll(String serverName);

    void registerBaseResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader);

    void registerExtResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader);
}
