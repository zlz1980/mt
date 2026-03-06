package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.utils.PbThreadLocal;

/**
 * 扩展pbSequence生成接口
 * @author JiangTaiSheng
 */
public interface PbSequenceHandler {

    /**
     * bizId生成
     */
    default String generate() {
        return PbThreadLocal.get().getBizId();
    }
}
