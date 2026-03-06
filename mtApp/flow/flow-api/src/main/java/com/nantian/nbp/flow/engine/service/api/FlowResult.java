/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.api;

import com.nantian.nbp.base.model.PbLog;
import com.nantian.nbp.flow.engine.service.api.context.PbHeader;
import com.nantian.nbp.flow.engine.service.api.context.PbScope;
import com.nantian.nbp.flow.engine.service.api.context.Result;

/**
 * 流程处理对象
 * @author Administrator
 */
public class FlowResult extends Result {

	/** 业务编号 */
	private final String bizId;
	/** 响应报文 */
	private String res;
	/** 输出头 */
	private PbHeader outHeader;
	/** 输出体 */
	private PbScope<Object> outScope;
    /** 账号 */
    private String acctNo;
    /** 响应码 */
    private String instRespCode;

    public FlowResult(String bizId) {
        this.bizId = bizId;
    }

	public String getBizId() {
		return bizId;
	}

	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}

	public PbScope<Object> getOutScope() {
		return outScope;
	}

	public void setOutScope(PbScope<Object> outScope) {
		this.outScope = outScope;
	}

	public PbHeader getOutHeader() {
		return outHeader;
	}

	public void setOutHeader(PbHeader outHeader) {
		this.outHeader = outHeader;
	}

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getInstRespCode() {
        return instRespCode;
    }

    public void setInstRespCode(String instRespCode) {
        this.instRespCode = instRespCode;
    }
}
