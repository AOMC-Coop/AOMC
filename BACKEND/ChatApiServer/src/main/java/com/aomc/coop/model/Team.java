package com.aomc.coop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Team implements Serializable {
    private int idx;
    private String name;
    private Date create_date;
    private Date update_date;
    private int status; //0이면 비활성화

    private List<User> users;
    private List<Channel> channels;
    private String owner;
}
