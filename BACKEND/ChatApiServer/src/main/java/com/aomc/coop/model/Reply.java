package com.aomc.coop.model;

import lombok.Data;

@Data
public class Reply {
    private int idx;
    private String content;
    private User user;
    private Data send_date;
}
