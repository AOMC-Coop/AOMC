package com.aomc.coop.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserWithToken implements Serializable {
    private String uid;
    private String token;
}
