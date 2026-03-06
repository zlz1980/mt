package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.base.model.SagaTransitionManager;
import com.nantian.nbp.ev.PbVal;
import com.nantian.nbp.flow.engine.service.api.context.PbHeader;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;

public interface TranContext {
    PbVal getCtxVal(String key);

    PbScope<Object> getTranScope();

    PbScope<Object> getSysScope();

    PbHeader getInHeaderScope();

    PbScope<Object> getTmpScope();

    PbScope<Object> getInScope();

    PbScope<Object> getScopeVal(String key);

    Boolean getCtxBoolVal(String key);

    Object getValue(String key);

    SagaTransitionManager getSagaTransitionManager();

    String getFeTranCode();

    PbLog getPbLog();

    String printElVals(String key);
}
