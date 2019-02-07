package com.aomc.coop.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserWithToken implements Serializable {
    private int idx;
    private String token;
}
