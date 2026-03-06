package com.n.fbsp.atom.platform.seed.db.seq;

import java.sql.Timestamp;

public class SeqNum {
    private long seqId;
    private String seqGroup;
    private String seqKey;
    private long seqVal;
    private long initVal;
    private long maxVal;
    private Timestamp createTime;
    private int seqExpire;
    private long remainingTime;

    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public String getSeqGroup() {
        return seqGroup;
    }

    public void setSeqGroup(String seqGroup) {
        this.seqGroup = seqGroup;
    }

    public String getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(String seqKey) {
        this.seqKey = seqKey;
    }

    public long getSeqVal() {
        return seqVal;
    }

    public void setSeqVal(long seqVal) {
        this.seqVal = seqVal;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getSeqExpire() {
        return seqExpire;
    }

    public void setSeqExpire(int seqExpire) {
        this.seqExpire = seqExpire;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime){
        this.remainingTime = remainingTime;
    }

    public long getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(long maxVal) {
        this.maxVal = maxVal;
    }

    public long getInitVal() {
        return initVal;
    }

    public void setInitVal(long initVal) {
        this.initVal = initVal;
    }
}
