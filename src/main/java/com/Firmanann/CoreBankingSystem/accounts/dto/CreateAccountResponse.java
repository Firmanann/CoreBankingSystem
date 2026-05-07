package com.Firmanann.CoreBankingSystem.accounts.dto;


import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAccountResponse {

    private String accountNumber;
    private BigDecimal balance;
    private AccountStatus status;
    private Instant createdAt;
}
