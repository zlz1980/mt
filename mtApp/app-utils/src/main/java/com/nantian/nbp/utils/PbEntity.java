package com.nantian.nbp.utils;

/**
 * @author Administrator
 */
public class PbEntity {

    private String bizId;

    public PbEntity() {
    }

    public PbEntity(String bizId){
        this.bizId = bizId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }
}
