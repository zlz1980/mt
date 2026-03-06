package com.nantian.nbp.flow.engine.service.api.context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Administrator
 */
public class PbScope<T> extends LinkedHashMap<String,T> {

    public PbScope(){}

    public PbScope(int initialCapacity){super(initialCapacity);}

    public PbScope(Map<String,T> map){
        super(map);
    }
}
