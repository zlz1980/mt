package com.nantian.nbp.utils;

/**
 * @author Administrator
 */
public class PbThreadLocal {
    private static final ThreadLocal<PbEntity> LOCAL = new ThreadLocal<>();

    public static PbEntity get(){
        return LOCAL.get();
    }

    public static void set(PbEntity val){
        LOCAL.set(val);
    }

    public static void clear(){
        LOCAL.remove();
    }
}
