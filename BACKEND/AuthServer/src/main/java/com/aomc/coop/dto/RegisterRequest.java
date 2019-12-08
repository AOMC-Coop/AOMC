package com.aomc.coop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    private String uid;
    private String pwd;
    private String confirm_pwd;
    private String nickname;
    private String invite_token;
}
