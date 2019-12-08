package com.aomc.coop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProfileRequest {
    private int idx;
    private String nickname;
}