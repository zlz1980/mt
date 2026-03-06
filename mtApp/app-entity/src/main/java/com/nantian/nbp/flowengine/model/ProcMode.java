package com.nantian.nbp.flowengine.model;

public class ProcMode extends AstModel{
    /** proc分支 */
    private AstList procList = null;

    public AstList getProcList() {
        return procList;
    }

    public void setProcList(AstList procList) {
        this.procList = procList;
    }
}