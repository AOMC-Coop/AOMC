package com.aomc.coop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileInfo {
    private int idx;
    private String url;
    private String type;
    private String name;
}
