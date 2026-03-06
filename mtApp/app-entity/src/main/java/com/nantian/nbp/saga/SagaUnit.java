package com.nantian.nbp.saga;

import java.io.Serializable;
import java.util.List;

public class SagaUnit implements Serializable {
    /** 序号 */
    private String atomTranNo;
    /** 事务单参 */
    private String param;
    /** 事务多参 */
    private List<String> paramList;

    public SagaUnit(){}

    public SagaUnit(String atomTranNo, String param, List<String> paramList) {
        this.atomTranNo = atomTranNo;
        this.param = param;
        this.paramList = paramList;
    }

    public String getAtomTranNo() {
        return atomTranNo;
    }

    public String getParam() {
        return param;
    }

    public List<String> getParamList() {
        return paramList;
    }

}
