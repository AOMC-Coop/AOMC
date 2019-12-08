package com.aomc.coop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class NewPasswordRequest {
    private int idx;
    private String pwd;
    private String confirm_pwd;
}