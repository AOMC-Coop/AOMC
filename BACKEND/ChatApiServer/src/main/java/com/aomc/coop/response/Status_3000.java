package com.aomc.coop.response;

public enum Status_3000 {

    SUCCESS_Signup("3201"),
    FAIL_Signup("3401");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }

}
