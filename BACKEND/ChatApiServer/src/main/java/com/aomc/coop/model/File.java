package com.aomc.coop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class File {
    private int idx;
    private String content;
    private String type;
    private String name;
}
