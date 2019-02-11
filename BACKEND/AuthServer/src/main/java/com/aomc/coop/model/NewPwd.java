package com.aomc.coop.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class NewPwd implements Serializable {
    private int idx;
    private String pwd;
    private String confirm_pwd;
}
