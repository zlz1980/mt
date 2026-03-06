package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.TranCode;
import com.nantian.nbp.base.model.TranCodeConv;
import com.nantian.nbp.flowengine.model.FlowContainer;

import java.util.Map;

/**
 * @author Administrator
 */
public interface FlowEngineService<T> {

    default FlowResult executeService(TranCodeConv tcc, String bizId, Map<String, String> inHeader, String inData, T response) {
        return null;
    }

    /**
     * 流程执行主服务
     *
     * @param tranCode 交易码
     * @param config   执行配置
     * @param flowContainer 流程
     * @return FlowResult
     */
    FlowResult executeService(TranCode tranCode, ExecuteConfig config, FlowContainer flowContainer,T response);
}
