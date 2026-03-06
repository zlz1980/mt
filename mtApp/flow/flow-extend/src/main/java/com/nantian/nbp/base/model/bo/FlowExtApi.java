package com.nantian.nbp.base.model.bo;

import com.nantian.nbp.base.model.ExtApi;
import com.nantian.nbp.base.model.FlowKey;

/**
 * @ Author wushengchao
 * @ Date 2025/4/8
 **/
public class FlowExtApi {
    private FlowKey flowKey;
    private ExtApi request;
    private ExtApi response;
    private ExtApi error;

    public FlowKey getFlowKey() {
        return flowKey;
    }

    public void setFlowKey(FlowKey flowKey) {
        this.flowKey = flowKey;
    }

    public ExtApi getRequest() {
        return request;
    }

    public void setRequest(ExtApi request) {
        this.request = request;
    }

    public ExtApi getResponse() {
        return response;
    }

    public void setResponse(ExtApi response) {
        this.response = response;
    }

    public ExtApi getError() {
        return error;
    }

    public void setError(ExtApi error) {
        this.error = error;
    }
}
