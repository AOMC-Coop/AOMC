package com.aomc.coop.response;

public enum Status_1000 {

    SUCCESS_CREATE_TEAM("1201"),
    FAIL_CREATE_TEAM("1400"),

    SUCCESS_CREATE_Channel("1202"),
    FAIL_CREATE_Channel("1401"),

    SUCCESS_Update_Channel("1203"),
    FAIL_Update_Channel("1402"),

    SUCCESS_Get_Message("1204"),
    FAIL_Get_Message("1403"),
    No_Message("5503"),


    SUCCESS_Get_Channel_Users("1205"),
    FAIL_Get_Channel_Users("1404"),

    SUCCESS_Invite_Channel_User("1206"),
    Channel_Already_Has_User("1306"),
    FAIL_Invite_Channel_User("1405"),

    SUCCESS_Delete_Channel_User("1207"),
    FAIL_Delete_Channel_User("1406");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_1000(String status) {
        this.status = status;
    }
}
