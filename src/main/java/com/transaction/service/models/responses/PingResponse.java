package com.transaction.service.models.responses;

public class PingResponse {
    private String serverTime;

    public PingResponse(String serverTime) {
        this.serverTime = serverTime;
    }


    public String getServerTime() {
        return this.serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }


    @Override
    public String toString() {
        return "{" +
            " serverTime='" + getServerTime() + "'" +
            "}";
    }

}
