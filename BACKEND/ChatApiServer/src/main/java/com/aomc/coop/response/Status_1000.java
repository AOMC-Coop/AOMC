package com.aomc.coop.response;

public enum Status_1000 {

    FAIL_CREATE_TEAM("1400");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_1000(String status){
        this.status = status;
    }
}
