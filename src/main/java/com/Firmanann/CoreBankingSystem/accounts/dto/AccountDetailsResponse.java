package com.Firmanann.CoreBankingSystem.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDetailsResponse {

    private String accountNumber;
    private BigDecimal balance;
    private String username;

}
