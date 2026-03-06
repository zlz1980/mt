package com.nantian.nbp.main.filter;

import com.nantian.nbp.utils.StrUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * 应用相关信息
 * @author Administrator
 */
public class AppInfo {

    private volatile LongAdder curReqNum = new LongAdder();

    private volatile LongAdder curAsyncNum = new LongAdder();

    @Value("${health.flag:true}")
    private volatile String healthFlag;

    private final String hostName;

    public AppInfo() {
        Map<String, String> map = System.getenv();
        hostName = StrUtils.toStr(map.get("MY_HOST_NAME"),"Default");
    }

    public LongAdder getCurReqNum() {
        return curReqNum;
    }

    public void setCurReqNum(LongAdder curReqNum) {
        this.curReqNum = curReqNum;
    }

    public String getHealthFlag() {
        return healthFlag;
    }

    public void setHealthFlag(String healthFlag) {
        this.healthFlag = healthFlag;
    }

    public String getHostName() {
        return this.hostName;
    }

    public LongAdder getCurAsyncNum() {
        return curAsyncNum;
    }

    public void setCurAsyncNum(LongAdder curAsyncNum) {
        this.curAsyncNum = curAsyncNum;
    }
}
