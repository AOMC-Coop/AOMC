package com.aomc.coop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MessageRequest {
    private int message_idx;
    private String content;
    private String nickname;
    private int channel_idx;
    private int user_idx;
    private String send_date;
    private String send_time;
    private String send_db_date;
    private String file_url;
    private String file_name;
    private String type;
    private Reply reply;
    private String image;
}
