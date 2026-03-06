package com.nantian.nbp.base.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Flow implements Serializable {

    /** 流程Key */
    private FlowKey key;
    /** 原流程数据 */
    private List<FlowUnit> stepList;

    public Flow(){};

    public Flow(FlowKey key) {
        this.key = key;
        this.stepList = new LinkedList<>();
    }

    public Flow(FlowKey key,List<FlowUnit> stepList) {
        this.key = key;
        this.stepList = stepList;
    }

    public FlowKey getKey() {
        return key;
    }

    public void setKey(FlowKey key) {
        this.key = key;
    }

    public List<FlowUnit> getStepList() {
        return stepList;
    }

    public void setStepList(List<FlowUnit> stepList) {
        this.stepList = stepList;
    }

    public void addStep(FlowUnit step) {
        stepList.add(step);
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
