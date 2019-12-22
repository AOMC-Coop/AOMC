package com.aomc.coop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Reply {
    private int idx;
    private String content;
    private User user;
    private Date send_date;
}
