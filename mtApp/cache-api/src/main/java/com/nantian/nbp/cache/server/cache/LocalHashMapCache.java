package com.nantian.nbp.cache.server.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class LocalHashMapCache<T> implements BaseCache<T>{

    private final String cacheName;

    private final ConcurrentHashMap<String,T> cacheSource;

    public ConcurrentHashMap<String, T> getCacheSource() {
        return cacheSource;
    }

    public LocalHashMapCache(String cacheName) {
        this.cacheName = cacheName;
        cacheSource = new ConcurrentHashMap<>();
    }

    public LocalHashMapCache(String cacheName, int initialCapacity) {
        this.cacheName = cacheName;
        cacheSource = new ConcurrentHashMap<>(initialCapacity);
    }

    @Override
    public void put(String id,T val){
        cacheSource.put(id, val);
    }
    @Override
    public T get(String id){
        return cacheSource.get(id);
    }
    @Override
    public void remove(String id){
        cacheSource.remove(id);
    }
    @Override
    public boolean containsKey(String id){
        return cacheSource.containsKey(id);
    }
    @Override
    public void clear(){
        cacheSource.clear();
    }
    @Override
    public Collection<T> values(){
        return cacheSource.values();
    }
    @Override
    public String getCacheName() {
        return cacheName;
    }
}
