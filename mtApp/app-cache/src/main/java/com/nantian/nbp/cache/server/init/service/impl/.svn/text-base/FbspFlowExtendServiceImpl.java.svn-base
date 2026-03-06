package com.nantian.nbp.cache.server.init.service.impl;

import com.nantian.nbp.base.model.Flow;
import com.nantian.nbp.base.model.FlowKey;
import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.cache.server.cache.PbCache;
import com.nantian.nbp.flow.extend.AbstractFlowExtendService;

import java.util.Map;
import java.util.function.Supplier;

import static com.nantian.nbp.cache.server.api.Constants.TRAN_CODE_MSG_KEY;

/**
 * @author Administrator
 */
public class FbspFlowExtendServiceImpl extends AbstractFlowExtendService {

    private final Supplier<PbCache> pbCacheSupplier;

    private Map<FlowKey, Flow> flowMap;

    public FbspFlowExtendServiceImpl(Supplier<PbCache> pbCacheSupplier) {
        this.pbCacheSupplier = pbCacheSupplier;
    }

    public Map<FlowKey, Flow> getFlowMap() {
        return flowMap;
    }

    public void setFlowMap(Map<FlowKey, Flow> flowMap) {
        this.flowMap = flowMap;
    }

    @Override
    public TranCode findTranCode(FlowKey flowKey) {
        return cache().get(TRAN_CODE_MSG_KEY ,flowKey.toString());
    }

    @Override
    public Flow findOriginalFlowByKey(FlowKey flowKey){
        if(flowMap != null) {
            return flowMap.get(flowKey);
        }
        return null;
    }

    private PbCache cache() {
        return pbCacheSupplier.get();
    }

}
