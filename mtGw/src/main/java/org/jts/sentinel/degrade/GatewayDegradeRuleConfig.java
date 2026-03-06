package org.jts.sentinel.degrade;

import static com.alibaba.csp.sentinel.slots.block.RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO;

public class GatewayDegradeRuleConfig {

    private String resourceId;
    private Integer grade = DEGRADE_GRADE_EXCEPTION_RATIO ;
    private double count;
    private Integer timeWindow;
    private Integer minRequestAmount;
    private Integer statIntervalMs = 1000;
    private Double slowRatioThreshold = 0.0;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public Integer getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(Integer timeWindow) {
        this.timeWindow = timeWindow;
    }

    public Integer getMinRequestAmount() {
        return minRequestAmount;
    }

    public void setMinRequestAmount(Integer minRequestAmount) {
        this.minRequestAmount = minRequestAmount;
    }

    public Integer getStatIntervalMs() {
        return statIntervalMs;
    }

    public void setStatIntervalMs(Integer statIntervalMs) {
        this.statIntervalMs = statIntervalMs;
    }

    public Double getSlowRatioThreshold() {
        return slowRatioThreshold;
    }

    public void setSlowRatioThreshold(Double slowRatioThreshold) {
        this.slowRatioThreshold = slowRatioThreshold;
    }
}
