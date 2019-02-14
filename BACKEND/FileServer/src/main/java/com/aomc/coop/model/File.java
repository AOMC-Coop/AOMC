package com.aomc.coop.model;

import lombok.Data;

import java.util.Date;

@Data
public class File {
    private int idx;
    private String url;
    private String type;
    private String name;
}
