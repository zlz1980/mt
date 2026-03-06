package com.nantian.nbp.base.model;

/**
 * 流程中请求服务方状态记录
 * @Author : Liang Haizhen
 * @Date 2024/9/19 11:04
**/

public class ServiceStatus {

	/**
	 * 流程中请求服务方状态记录
	 */
	private String serName = null;
	private String serCode = null;
	private String serMsg = null;
	private String serTime = null;
	private String currStep = null;

	public String getSerTime() {
		return serTime;
	}

	public void setSerTime(String serTime) {
		this.serTime = serTime;
	}

	public String getSerName() {
		return serName;
	}

	public void setSerName(String serName) {
		this.serName = serName;
	}

	public String getSerCode() {
		return serCode;
	}

	public void setSerCode(String serCode) {
		this.serCode = serCode;
	}

	public String getSerMsg() {
		return serMsg;
	}

	public void setSerMsg(String serMsg) {
		this.serMsg = serMsg;
	}

	public String getCurrStep() {
		return currStep;
	}

	public void setCurrStep(String currStep) {
		this.currStep = currStep;
	}

	@Override
	public String toString() {
		return "ServiceStatus{" +
				"serName='" + serName + '\'' +
				", serCode='" + serCode + '\'' +
				", serMsg='" + serMsg + '\'' +
				", serTime='" + serTime + '\'' +
				", currStep='" + currStep + '\'' +
				'}';
	}
}
