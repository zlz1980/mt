package org.jts.cache.local;

import org.jts.cache.BaseCache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class LocalHashMapCache<T> implements BaseCache<T> {

    private final String cacheName;

    private final ConcurrentHashMap<String,T> cacheSource;

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
        cacheSource.put(id.toUpperCase(), val);
    }
    @Override
    public T get(String id){
        return cacheSource.get(id.toUpperCase());
    }
    @Override
    public void remove(String id){
        cacheSource.remove(id.toUpperCase());
    }
    @Override
    public boolean containsKey(String id){
        return cacheSource.containsKey(id.toUpperCase());
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
