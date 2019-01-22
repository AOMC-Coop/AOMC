package com.aomc.coop.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private int idx;
    private String content;
    private User user;
    private Data send_date;

    private File file;
    private Reply reply;
}
