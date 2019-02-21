package com.aomc.coop.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private int idx;
    private String uid;
    private String pwd;
    private String confirm_pwd;
    private String salt;
    private String nickname;
    private String role;
    private int gender;
    private int status;
    private Date reg_date;
    private Date access_date;
    private Date update_date;
    private String image;
    private String invite_token;
}
