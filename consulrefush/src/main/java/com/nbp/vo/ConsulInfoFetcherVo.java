package com.nbp.vo;


public class ConsulInfoFetcherVo {
    private String service;
    private String port;
    private String address;
    private String azflag;

    public String getAzflag() {
        return azflag;
    }

    public void setAzflag(String azflag) {
        this.azflag = azflag;
    }

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
