package org.jts.cache.loader;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteCacheLock {
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private ReadWriteCacheLock(){}

    public static ReadWriteLock instance(){
        return LOCK;
    }

    public static void readLock(){
        LOCK.readLock().lock();
    }

    public static void readUnlock(){
        LOCK.readLock().unlock();
    }

    public static void writeLock(){
        LOCK.writeLock().lock();
    }

    public static void writeUnlock(){
        LOCK.writeLock().unlock();
    }
}
