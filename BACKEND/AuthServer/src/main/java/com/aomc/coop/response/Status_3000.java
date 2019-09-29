package com.aomc.coop.response;

public enum Status_3000 {
    SUCCESS_Register("3201"),
    SUCCESS_Register_Auth_Mail_Sent("3202"),
    FAIL_Register("3401"),
    FAIL_Register_Duplicate("3402"),
    FAIL_Register_Timeout("3403"),

    SUCCESS_Withdrawal("3210"),
    FAIL_Withdrawal("3410"),

    SUCCESS_Login("3220"),
    FAIL_Login("3420"),
    FAIL_Login_Wrong_ID("3421"),
    FAIL_Login_Wrong_Password("3422"),
    FAIL_Login_Withdrawal("3423"),

    SUCCESS_Logout("3230"),
    FAIL_Logout("3430"),

    SUCCESS_Get_Profile("3240"),
    FAIL_Get_Profile("3440"),
    FAIL_Get_Profile_Wrong_Idx("3441"),

    SUCCESS_Set_Profile("3250"),
    FAIL_Set_Profile("3450"),
    FAIL_Set_Profile_Wrong_Idx("3451"),

    SUCCESS_Change_Pwd("3260"),
    FAIL_Change_Pwd("3460"),

    SUCCESS_Missing_Pwd_Email_Auth("3270"),
    FAIL_Missing_Pwd_Email_Auth("3470");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }
}
