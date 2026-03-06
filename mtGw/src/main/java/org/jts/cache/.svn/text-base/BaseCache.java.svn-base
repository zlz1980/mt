package org.jts.cache;

import java.util.Collection;

public interface BaseCache<T> {

    void put(String id, T val);

    T get(String id);

    void remove(String id);

    boolean containsKey(String id);

    void clear();

    Collection<T> values();

    String getCacheName();
}
