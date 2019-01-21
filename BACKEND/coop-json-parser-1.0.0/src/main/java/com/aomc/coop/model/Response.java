package com.aomc.coop.model;

import lombok.Data;

@Data
public class Response {
    private int status;
    private String message;
    private String description;
}