package com.aomc.coop.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
// [면접] : Reducing Boilerplate Code with Project Lombok
public class User implements Serializable {
// [면접] : 자바 시스템 내부에서 사용되는 Object 또는 Data를 외부의 자바 시스템에서도 사용할 수 있도록 byte 형태로 데이터를 변환하는 기술
// JVM(Java Virtual Machine 이하 JVM)의 메모리에 상주(힙 또는 스택)되어 있는 객체 데이터를 바이트 형태로 변환하는 기술

    private int idx;
    private String uid;
    private String pwd;
    private String confirm_pwd;
    private String salt;
    private String nickname;
    private String role;
    private int status;
    private Date reg_date;
    private Date access_date;
    private Date update_date;
    private String image;
    private String invite_token;
}
