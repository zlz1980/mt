package com.nantian.nbp.saga;

public class SagaRollbackFlow {
    /** 业务编号 */
    private String bizId;
    /** 渠道号 */
    private String chnlNo;
    /** 外部交易码 */
    private String fTranCode;
    /** 业务种类 */
    private String bizType;
    /** 内部交易码 */
    private String tranCode;
    /** 状态 */
    private char status;
    /** 执行次数 */
    private int execTimes = 0;
    private boolean isCommit = false;
    public SagaRollbackFlow (){}

    public SagaRollbackFlow(String bizId, String chnlNo, String fTranCode, String bizType, String tranCode,
                            char status,boolean isCommit) {
        this.bizId = bizId;
        this.chnlNo = chnlNo;
        this.fTranCode = fTranCode;
        this.bizType = bizType;
        this.tranCode = tranCode;
        this.status = status;
        this.isCommit = isCommit;
    }

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

    public String getChnlNo() {
        return chnlNo;
    }

    public void setChnlNo(String chnlNo) {
        this.chnlNo = chnlNo;
    }

    public String getfTranCode() {
        return fTranCode;
    }

    public void setfTranCode(String fTranCode) {
        this.fTranCode = fTranCode;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(int execTimes) {
        this.execTimes = execTimes;
    }

    @Override
    public String toString() {
        return "SagaRollbackFlow{" +
                "bizId='" + bizId + '\'' +
                ", chnlNo='" + chnlNo + '\'' +
                ", fTranCode='" + fTranCode + '\'' +
                ", bizType='" + bizType + '\'' +
                ", tranCode='" + tranCode + '\'' +
                ", status=" + status +
                ", execTimes=" + execTimes +
                ", isCommit=" + isCommit +
                '}';
    }
}
