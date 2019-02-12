package com.aomc.coop.response;

public enum Status_common {

    BAD_REQUEST("400"),
    INTERNAL_SERVER_ERROR("500"),

    Fail_Authorization("401");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_common(String status) {
        this.status = status;
    }

}
