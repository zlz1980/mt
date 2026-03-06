package com.nantian.nbp.flowengine.model;

import com.nantian.nbp.base.model.Flow;

import java.io.Serializable;

public class FlowContainer implements Serializable {

    /** 原流程数据 */
    private Flow flow;
    /** 加工后执行数据 */
    private AstList execList;

    public AstList getExecList() {
        return execList;
    }


    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }


    public void setExecList(AstList execList) {
        this.execList = execList;
    }

    public boolean isEmpty() {
        return execList.isEmpty();
    }

}
