package com.aomc.coop.response;

public enum Status_5000 {
    SUCCESS_UPDATE_TEAM("5201"),
    SUCCESS_DELETE_TEAM("5202"),
    SUCCESS_READ_TEAM("5203"),
    SUCCESS_INVITE("5204"),
    SUCCESS_READ_CHANNEL("5205"),
    SUCCESS_DEACVITE_USER("5206"),

    FAIL_UPDATE_TEAM("5401"),
    FAIL_DELETE_TEAM("5402"),
    FAIL_READ("5403"),
    DEACTIVE_TEAM("5404"),
    FAIL_DEACVITE_USER("5406");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_5000(String status) {
        this.status = status;
    }
}
