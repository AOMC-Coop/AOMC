package com.aomc.coop.dto;

import com.aomc.coop.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WithdrawalRequest {
    private int idx;
    private int status = 0;
}
