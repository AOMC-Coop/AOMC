package com.aomc.coop.model;

import lombok.Data;

@Data
public class Message {
    private int idx;
    private String content;
    //채널?
    private User user;
    private Data send_date;

    private File file;
    private Reply reply;
}
