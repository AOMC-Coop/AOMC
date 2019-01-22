package com.aomc.coop.response;

public enum Status_5000 {
    SUCCESS_UPDATE_TEAM("5201"),
    SUCCESS_DELETE_TEAM("5202"),
    FAIL_UPDATE_TEAM("5401"),
    FAIL_DELETE_TEAM("5402");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_5000(String status) {
        this.status = status;
    }
}
