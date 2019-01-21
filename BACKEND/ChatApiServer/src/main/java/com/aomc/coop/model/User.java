package com.aomc.coop.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int idx;
    private String uid;
    private String pwd;
    private String salt;
    private String nickname;
    private int gender;
    private int role;
    private String status;
    private Date reg_date;
    private Date access_date;
    private Date update_date;
}
