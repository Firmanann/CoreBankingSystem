package com.Firmanann.CoreBankingSystem.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponse {

    private String referenceNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private BigDecimal currentBalance;
    private Instant timestamp;
}