package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.FlowUnit;

import java.util.Map;

public interface HttpSvcClientTemplate<T> {


    default Map<String, String> beforeHeader(TranContext tranContext, ScopeValUnit scopeValUnit,
            FlowUnit flowUnit, Map<String, String> header) {
        return header;
    }

    default void beforeBody(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit, T body) {
    }

    default void afterHeader(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit,
                             Map<String, String> header) {
    }

    default void afterBody(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit, T body) {
    }

}
