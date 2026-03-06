package com.nantian.nbp.flow.engine.service.api.saga;

import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.RetType;

/**
 * 扩展事务结果确认机制
 * @author JiangTaiSheng
 */
public interface SagaTransitionResConfirmation {

    /**
     * 扩展事务结果确认机制，根据流程返回结果信息，自定义逻辑判断是否确认事务执行成功
     * @param out 流程返回信息，以return的返回结果作为输入
     * @return boolean 事务是否执行成功
     */
    default SagaTransitionResult isResConfirmed(PbScope<Object> out){
        SagaTransitionResult result = new SagaTransitionResult();
        result.setRetType(RetType.SUCCESS);
        return result;
    }
}
