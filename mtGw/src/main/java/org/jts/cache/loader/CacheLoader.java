/**
* Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
* Finance Business Service Platform (FBSP)
*
* @author:NANTIAN
* @date:pengw at Jul 28, 2020 2:13:59 PM
*
*/
package org.jts.cache.loader;


import org.jts.cache.local.PbCache;
import org.jts.cache.resource.ResourceCacheLoader;

public interface CacheLoader {

	PbCache getMainPbCache();

    PbCache getFlushingPbCache();

    void initAll(String serverName);

    boolean reloadAll(String serverName);

    void registerBaseResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader);

    void registerExtResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader);

}
