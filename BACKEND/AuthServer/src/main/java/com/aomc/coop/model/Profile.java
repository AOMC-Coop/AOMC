package com.aomc.coop.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Profile implements Serializable {
    private int idx;
    private String uid;
    private String nickname;
    private int gender;
    // ***** 프로필 사진 정보 추가해야 함
}