package com.aomc.coop.response;

public enum Status_3000 {
// *** 다 끝나고 나서 한 번 손보긴 해야함 -> 더욱 자세히 case 나누기

    // 3200 ~ 3249 : 인증 서버 / 성공
    // 3400 ~ 3449 : 인증 서버 / 실패

    SUCCESS_Register("3201"),
    FAIL_Register("3401"),
    FAIL_Register_Duplicate("3402"),

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
    FAIL_Set_Profile_Wrong_Idx("3411"),

    // 3250 ~ 3299 : 파일 서버 / 성공
    // 3450 ~ 3499 : 파일 서버 / 실패

    SUCCESS_File_Upload("3250"),
    FAIL_File_Upload("3450"),

    SUCCESS_Profile_Picture_Upload("3251"),
    FAIL_Profile_Picture_Upload("3451");

    final private String status;

    public String getStatus() {
        return status;
    }

    private Status_3000(String status) {
        this.status = status;
    }
}
