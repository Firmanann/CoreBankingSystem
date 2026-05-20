package com.Firmanann.CoreBankingSystem.transactions.dto;

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
public class TransferResponse {

    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String referenceNumber;
    private BigDecimal amount;
    private BigDecimal currentBalanceSourceAccountNumber;
    private Instant createdAt;
}
