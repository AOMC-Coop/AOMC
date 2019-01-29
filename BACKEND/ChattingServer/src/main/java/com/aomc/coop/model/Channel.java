package com.aomc.coop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Channel implements Serializable {
    private int idx;
    private String name;
    private List<User> users;
    private Date create_date;
    private int star_flag;
    private int status;
    private int teamIdx;

    @JsonIgnore
    private List<Message> messages; //큐인지 리스트인지

}
