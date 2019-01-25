package com.aomc.coop.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {
    private int idx;
    private String content;
    private String nickname;
    private String send_date;
    private String send_time;

    private File file;
    private Reply reply;
}
