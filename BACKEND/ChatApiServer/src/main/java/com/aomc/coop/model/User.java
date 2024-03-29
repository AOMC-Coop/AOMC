package com.aomc.coop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data

public class User implements Serializable {
    private int idx;
    private String uid;
    private String pwd;
    private String salt;
    private String nickname;
    private String image;
    private int gender;
    private int role;
    private int status;
    private Date reg_date;
    private Date access_date;
    private Date update_date;
}
