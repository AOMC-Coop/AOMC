package com.aomc.coop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class Reply {
    private int idx;
    private String content;
    private User user;
    private Date send_date;
}
