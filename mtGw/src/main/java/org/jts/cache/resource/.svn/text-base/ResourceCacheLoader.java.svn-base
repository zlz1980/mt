package org.jts.cache.resource;


public abstract class ResourceCacheLoader<T> {

    private final String resourceName;

    protected ResourceCacheLoader(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public abstract PbResourceCache<T> initCache();
}
