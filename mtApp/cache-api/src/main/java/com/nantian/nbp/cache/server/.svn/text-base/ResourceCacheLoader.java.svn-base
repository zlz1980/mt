package com.nantian.nbp.cache.server;

import com.nantian.nbp.cache.server.cache.PbResourceCache;

public abstract class ResourceCacheLoader<T> {

    private final String resourceName;

    protected ResourceCacheLoader(String resourceName) {
        this.resourceName = resourceName;
    }

    protected String getResourceName() {
        return resourceName;
    }

    protected abstract PbResourceCache<T> initCache();
}
