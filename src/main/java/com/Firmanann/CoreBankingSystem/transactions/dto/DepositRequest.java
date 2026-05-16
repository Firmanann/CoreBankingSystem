package com.Firmanann.CoreBankingSystem.transactions.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotBlank(message = "Nomor rekening tujuan tidak boleh kosong")
    private String targetAccountNumber;

    @NotNull(message = "Nominal deposit tidak boleh kosong")
    @DecimalMin(value = "10000.00", message = "Minimal deposit adalah Rp 10.000")
    private BigDecimal amount;

    // Optional
    private String description;
}