package com.aomc.coop.response;

public enum Status_3000 {

    SUCCESS_File_Upload("3280"),
    FAIL_File_Upload("3480"),

    SUCCESS_Profile_Picture_Upload("3290"),
    FAIL_Profile_Picture_Upload("3490");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }
}
