package com.nbp.check.checkout;

/**
 * 执行器参数实体类
 */
public class ExecutorParams {
    /**
     * 工程id
     */
    private String proIds;
    /**
     * 参数1
     */
    private String param1;
    /**
     * 参数2
     */
    private String param2;
    /**
     * 导出目录
     */
    private String filePath;

    public String getProIds() {
        return proIds;
    }

    public void setProIds(String proIds) {
        this.proIds = proIds;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
