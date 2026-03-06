/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package org.jts.cache.loader;

import org.jts.cache.local.PbCache;
import org.jts.cache.resource.PbResourceCache;
import org.jts.cache.resource.ResourceCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CacheLoaderManager implements CacheLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheLoaderManager.class);

    private PbCache mainPbCache;

    private PbCache slavePbCache;

    private PbCache flushingPbCache;

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    private final List<ResourceCacheLoader<?>> baseResLoaderList = new LinkedList<>();

    private final List<ResourceCacheLoader<?>> extResLoaderList = new LinkedList<>();

    private final long initCacheTimeout ;

    public CacheLoaderManager(long initCacheTimeout) {
        mainPbCache = new PbCache("mainCache", 20);
        slavePbCache = new PbCache("slaveCache", 20);
        this.initCacheTimeout = initCacheTimeout;
    }

    @Override
    public PbCache getMainPbCache() {
        ReadWriteCacheLock.readLock();
        try {
            return mainPbCache;
        }finally {
            ReadWriteCacheLock.readUnlock();
        }
    }

    @Override
    public PbCache getFlushingPbCache() {
        return flushingPbCache;
    }

    @Override
    public void registerBaseResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader){
        baseResLoaderList.add(resourceCacheLoader);
    }

    @Override
    public void registerExtResourceCacheLoader(ResourceCacheLoader<?> resourceCacheLoader) {
        extResLoaderList.add(resourceCacheLoader);
    }

    @Override
    public void initAll(String serverName) {
        LOGGER.info("Begin init cache. serverName[{}]", serverName);
        Consumer<ExecutorService> programShutdown = (executorService) -> {
            executorService.shutdownNow();
            System.exit(1);
        };
        flushingPbCache = mainPbCache;
        loadAllCacheResource(serverName, mainPbCache, executorService, programShutdown);
    }

    @Override
    public boolean reloadAll(String serverName) {
        LOGGER.info("Begin reload cache. serverName[{}]", serverName);
        flushingPbCache = slavePbCache;
        boolean flag = loadAllCacheResource(serverName, slavePbCache, executorService, null);
        if(flag) {
            PbCache hisCache = mainPbCache;
            ReadWriteCacheLock.writeLock();
            try {
                this.mainPbCache = slavePbCache;
                slavePbCache = hisCache;
            }finally {
                ReadWriteCacheLock.writeUnlock();
            }
            slavePbCache.clear();
        }
        return flag;
    }

    private boolean loadAllCacheResource(String serverName, PbCache pbCache, ExecutorService executorService,
                                         Consumer<ExecutorService> consumer) {
        long b = System.currentTimeMillis();
        AtomicBoolean initFlag = new AtomicBoolean(true);
        pbCache.clear();
        try {
            long a = System.currentTimeMillis();
            for(ResourceCacheLoader<?> resourceCacheLoader : baseResLoaderList) {
                PbResourceCache<?> resCache = resourceCacheLoader.initCache();
                if(Objects.nonNull(resCache)){
                    pbCache.put(resCache.getCacheName(), resCache);
                }else {
                    LOGGER.warn("Base resource cache[{}] is null",resourceCacheLoader.getResourceName());
                }
            }
            LOGGER.info("Base data cost[{}]ms", System.currentTimeMillis() - a);
        } catch (Exception e) {
            LOGGER.error("init cache baseResource error will shutdown", e);
            initFlag.set(false);
            if(consumer != null) {
                consumer.accept(executorService);
            }
        }
        return initFlag.get();
    }

}

