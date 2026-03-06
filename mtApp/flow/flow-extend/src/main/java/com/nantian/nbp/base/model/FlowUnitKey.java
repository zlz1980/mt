/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 */
package com.nantian.nbp.base.model;

import java.io.Serializable;
import java.util.Objects;


/**
 * 业务种类与交易码对应关系
 * @author pengw at Jul 26, 2020 12:07:37 AM
 * @version V2.0
 */
public class FlowUnitKey implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 业务种类 */
    private String busiType;
    /** 内部交易码 */
    private String tranCode;
    /** 步骤对应的命名空间名称 */
    private String ascope;

    public FlowUnitKey() {
        super();
    }

    public FlowUnitKey(String busiType, String tranCode, String ascope) {
        super();
        this.busiType = busiType;
        this.tranCode = tranCode;
        this.ascope = ascope;
    }

    public static FlowUnitKey of(String busiType, String tranCode, String ascope) {
        return new FlowUnitKey(busiType, tranCode, ascope);
    }

    public String getAscope() {
        return ascope;
    }

    public void setAscope(String ascope) {
        this.ascope = ascope;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    @Override
    public String toString() {
        return busiType + "_" + tranCode + "_" + ascope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowUnitKey that = (FlowUnitKey) o;
        return Objects.equals(ascope, that.ascope)
                && Objects.equals(busiType, that.busiType)
                && Objects.equals(tranCode, that.tranCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busiType, tranCode, ascope);
    }
}
