package com.aomc.coop.response;

public enum Status_3000 {
// *** 다 끝나고 나서 한 번 손보긴 해야함 -> 더욱 자세히 case 나누기
    SUCCESS_Register("3201"),
    SUCCESS_Register_Auth_Mail_Sent("3210"),
    FAIL_Register("3401"),
    FAIL_Register_Duplicate("3402"),
    FAIL_Register_Timeout("3412"),

    SUCCESS_Withdrawal("3202"),
    FAIL_Withdrawal("3403"),

    SUCCESS_Login("3203"),
    FAIL_Login("3404"),
    FAIL_Login_Wrong_ID("3405"),
    FAIL_Login_Wrong_Password("3406"),

    SUCCESS_Logout("3204"),
    FAIL_Logout("3407"),

    SUCCESS_Get_Profile("3205"),
    FAIL_Get_Profile("3408"),
    FAIL_Get_Profile_Wrong_Idx("3409"),

    SUCCESS_Set_Profile("3206"),
    FAIL_Set_Profile("3410"),
    FAIL_Set_Profile_Wrong_Idx("3411");


    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }
}
