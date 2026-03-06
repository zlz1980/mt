package com.nantian.nbp.base.model;

import com.nantian.nbp.saga.SagaRollbackFlow;
import com.nantian.nbp.saga.SagaRollbackStep;
import com.nantian.nbp.utils.StrUtils;

import java.util.LinkedList;
import java.util.List;

public class SagaTransitionManager {

    private final SagaRollbackFlow sagaRollbackFlow;
    private final List<SagaRollbackStep> stepList = new LinkedList<>();
    private boolean triggerFlag = Boolean.FALSE;
    private int index = 1;

    public SagaTransitionManager(String bizId, String chnlNo, String fTranCode, String bizType, String tranCode) {
        this.sagaRollbackFlow = new SagaRollbackFlow(bizId, chnlNo, fTranCode, bizType, tranCode, 'B', false);
    }

    public void addStep(SagaRollbackStep step) {
        step.setBizId(getBizId());
        step.setStepNo(StrUtils.toStrDefBlank(index));
        //原始状态'B'
        step.setStatus('B');
        step.setCommit(false);
        stepList.add(step);
        index++;
    }

    public boolean isNotEmpty() {
        return !stepList.isEmpty();
    }

    public boolean isTriggerFlag() {
        return triggerFlag;
    }

    public void setTriggerFlag(boolean triggerFlag) {
        this.triggerFlag = triggerFlag;
    }

    public SagaRollbackFlow getSagaRollbackFlow() {
        return sagaRollbackFlow;
    }

    public List<SagaRollbackStep> getStepList() {
        return stepList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBizId() {
        return sagaRollbackFlow.getBizId();
    }

    @Override
    public String toString() {
        return "SagaTransitionManager{" +
                "sagaRollbackFlow=" + sagaRollbackFlow +
                ", stepList=" + stepList +
                ", triggerFlag=" + triggerFlag +
                ", index=" + index +
                '}';
    }
}
