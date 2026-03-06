package com.nantian.nbp.saga;

public class SagaRollbackStep {
    /** 业务编号 */
    private String bizId;
    /** 步骤号 */
    private String stepNo;
    /** 输入Json信息串 */
    private String inParamJson;
    /** 事务流程号 */
    private String rbfTranCode;
    /** 状态 */
    private char status;
    private boolean isCommit;

    public boolean isCommit() {
        return isCommit;
    }

    public void setCommit(boolean commit) {
        isCommit = commit;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public String getInParamJson() {
        return inParamJson;
    }

    public void setInParamJson(String inParamJson) {
        this.inParamJson = inParamJson;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getRbfTranCode() {
        return rbfTranCode;
    }

    public void setRbfTranCode(String rbfTranCode) {
        this.rbfTranCode = rbfTranCode;
    }

    @Override
    public String toString() {
        return "SagaRollbackStep{" +
                "bizId='" + bizId + '\'' +
                ", stepNo='" + stepNo + '\'' +
                ", inParamJson='" + inParamJson + '\'' +
                ", rbfTranCode='" + rbfTranCode + '\'' +
                ", status=" + status +
                ", isCommit=" + isCommit +
                '}';
    }
}
