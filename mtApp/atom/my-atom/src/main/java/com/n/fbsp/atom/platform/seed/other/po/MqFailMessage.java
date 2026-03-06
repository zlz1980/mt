package com.n.fbsp.atom.platform.seed.other.po;


/**
 * @Author :
 * @create 2024/10/16 14:18
 */
public class MqFailMessage {
    private String mqId;
    private String topic;
    private String mqMessage;
    private String status;
    private String nodeId;
    private int execTimes = 0;
    private String chnlNo;
    private String fTranCode;

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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(int execTimes) {
        this.execTimes = execTimes;
    }

    public String getMqId() {
        return mqId;
    }

    public void setMqId(String mqId) {
        this.mqId = mqId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMqMessage() {
        return mqMessage;
    }

    public void setMqMessage(String mqMessage) {
        this.mqMessage = mqMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MqFailMessage{" +
                "chnlNo='" + chnlNo + '\'' +
                ", mqId='" + mqId + '\'' +
                ", topic='" + topic + '\'' +
                ", mqMessage='" + mqMessage + '\'' +
                ", status='" + status + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", execTimes=" + execTimes +
                ", fTranCode='" + fTranCode + '\'' +
                '}';
    }
}
