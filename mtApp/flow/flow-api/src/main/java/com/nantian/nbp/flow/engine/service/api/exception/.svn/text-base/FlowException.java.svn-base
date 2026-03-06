/**
 * Copyright(c) 2017-2020 NANTIAN Electronic Information Co., Ltd.
 * Finance Business Service Platform (FBSP)
 *
 * @author:NANTIAN
 * @date:pengw at Jul 28, 2020 2:13:59 PM
 *
 */
package com.nantian.nbp.flow.engine.service.api.exception;

/**
 * 流程处理技术异常描述类。
 * @author qujbin
 */
public class FlowException extends RuntimeException {
	private static final long serialVersionUID = 5312995928855391858L;
	/**
	 * 调用原子交易异常
	 */
	public static final String EFLW001="EFLW001";

	private final String errCode;
	private final String feTranCode;
	private final String message;
	private final String errStepNo;
	private final String atomTranCode;

	public FlowException(String message) {
		this(null, null, message,null,null, null);
	}

	public FlowException(String errCode,String errMsg) {
		this(errCode, null, errMsg, null);
	}

	public FlowException(String errCode, String errMsg, Throwable throwable) {
		this(errCode, null, errMsg,null,null, throwable);
	}

	public FlowException(String errCode, String feTranCode,String message, Throwable throwable) {
		this(errCode, feTranCode, message,null,null, throwable);
	}

	public FlowException(String errCode, String feTranCode, String message,String errStepNo,String atomTranCode,Throwable throwable) {
		super(message, throwable);
		this.feTranCode = feTranCode;
		this.errCode = errCode;
		this.message = message;
		this.errStepNo = errStepNo;
		this.atomTranCode = atomTranCode;
	}

	public String getFeTranCode(){
		return this.feTranCode;
	}

	public String getErrCode() {
		return errCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getErrStepNo() {
		return errStepNo;
	}

	public String getAtomTranCode() {
		return atomTranCode;
	}
}
