package com.Firmanann.CoreBankingSystem.transactions.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferRequest {

    @NotBlank(message = "Nomor rekening tidak boleh kosong")
    private String sourceAccountNumber;

    @NotBlank(message = "Nomor rekening tujuan tidak boleh kosong")
    private String targetAccountNumber;

    @NotNull(message = "Nominal transfer tidak boleh kosong")
    @DecimalMin(value = "10000.00", message = "Minimal transsfer adalah Rp 10.000")
    private BigDecimal amount;

    //optional
    private String description;


}
