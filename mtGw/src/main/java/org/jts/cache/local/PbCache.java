package org.jts.cache.local;

import org.jts.cache.resource.PbResourceCache;

public class PbCache extends LocalHashMapCache<PbResourceCache<?>> {

    public PbCache(String name) {
        super(name);
    }

    public PbCache(String name,int initialCapacity) {
        super(name,initialCapacity);
    }

    public void put(String id,PbResourceCache<?> val){
        super.put(id, val);
    }

    public PbResourceCache<?> get(String id){
        return super.get(id.toUpperCase());
    }

    public <T> T get(String resourceName, String id,Class<?> clz) {
        PbResourceCache<?> resourceCache = get(resourceName);
        if(resourceCache != null){
            return (T)resourceCache.get(id);
        }else {
            throw new RuntimeException(String.format("resourceName[%s] is null.",resourceName));
        }
    }
}