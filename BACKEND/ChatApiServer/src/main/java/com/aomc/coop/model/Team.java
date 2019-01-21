package com.aomc.coop.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Team {
    private int idx;
    private String name;
    private Date create_date;

    private List<User> users;
//    private List<Channel> channels;
//    private String owner;
}
