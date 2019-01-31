package com.aomc.coop.response;

public enum Status_3000 {

    SUCCESS_Register("3201"),
    FAIL_Register("3401"),
    FAIL_Register_Duplicate("3402"),

    SUCCESS_Login("3203"),
    FAIL_Login("3403"),
    FAIL_Login_Wrong_ID("3404"),
    FAIL_Login_Wrong_Password("3405");


    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }
}
