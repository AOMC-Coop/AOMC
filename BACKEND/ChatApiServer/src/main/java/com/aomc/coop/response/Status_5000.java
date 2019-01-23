package com.aomc.coop.response;

public enum Status_5000 {
    SUCCESS_UPDATE_TEAM("5201"),
    FAIL_UPDATE_TEAM("5401"),

    SUCCESS_DELETE_TEAM("5202"),
    FAIL_DELETE_TEAM("5402"),

    SUCCESS_READ_TEAM("5203"),
    FAIL_READ_TEAM("5502"),

    SUCCESS_INVITE("5204"),
    FAIL_INVITE("5405"),
    DEACTIVE_TEAM("5404"),

    SUCCESS_READ_CHANNEL("5205"),
    FAIL_READ_CHANNEL("5501"),

    SUCCESS_DEACVITE_USER("5206"),
    FAIL_DEACVITE_USER("5406"),

    SUCCESS_SEND_MAIL("5207"),
    FAIL_SEND_MAIL("5407"),

    FAIL_READ_USER("5502"),;

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_5000(String status) {
        this.status = status;
    }
}
