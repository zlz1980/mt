package com.nantian.nbp.flow.engine.service.api.exception;

/**
 * @author Administrator
 */
public class ThrowException extends FlowException{

    public ThrowException(String errCode, String errMsg) {
        super(errCode, null, errMsg, null);
    }
}
