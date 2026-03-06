package com.nantian.nbp.saga;

import com.nantian.nbp.base.model.SagaTransitionManager;

import java.util.concurrent.LinkedBlockingQueue;

public class SagaManageQueue extends LinkedBlockingQueue<SagaTransitionManager> {

    private static final SagaManageQueue INSTANCE = new SagaManageQueue();

    private SagaManageQueue(){}

    public static SagaManageQueue getInstance() {
        return INSTANCE;
    }

}
