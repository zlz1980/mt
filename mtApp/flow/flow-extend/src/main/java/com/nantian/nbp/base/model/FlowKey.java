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
public class FlowKey implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 业务种类 */
    private String busiType;
    /** 内部交易码 */
    private String tranCode;

    public FlowKey() {
        super();
    }

    public FlowKey(String busiType, String tranCode) {
        super();
        this.busiType = busiType;
        this.tranCode = tranCode;
    }

    public static FlowKey of(String busiType, String tranCode) {
        return new FlowKey(busiType, tranCode);
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
        return busiType + "_" + tranCode;
    }

    @Override
    public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
        FlowKey flowKey = (FlowKey) o;
        return Objects.equals(busiType, flowKey.busiType)
                && Objects.equals(tranCode, flowKey.tranCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busiType, tranCode);
    }
}
