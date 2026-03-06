package com.nantian.nbp.flow.engine.service.api.context;

public class SagaAtomResult extends AtomResult {

    private RetType busiRetType = RetType.UNKNOWN;;

    public SagaAtomResult() {
        super();
    }

    public SagaAtomResult(RetType retType) {
        super(retType);
    }

    public RetType getBusiRetType() {
        return busiRetType;
    }

    public void setBusiRetType(RetType busiRetType) {
        this.busiRetType = busiRetType;
    }
}
