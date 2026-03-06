package org.jts.cache.resource;

import org.jts.cache.local.LocalHashMapCache;

public class PbResourceCache<T> extends LocalHashMapCache<T> {

    public PbResourceCache(String name) {
        super(name);
    }

    public PbResourceCache(String name,int initialCapacity) {
        super(name,initialCapacity);
    }
}
