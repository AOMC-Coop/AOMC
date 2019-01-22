package com.aomc.coop.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Queue;

@Data
public class Channel {
    private int idx;
    private String name;
    private List<User> users;
    private Date create_date;
    private int star_flag;
    private int teamIdx;

    private List<Message> messages; //큐인지 리스트인지
//    private Queue<Message> messages;

}
