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
public class WithdrawRequest {

    @NotBlank(message = "Nomor rekening tidak boleh kosong")
    private String sourceAccountNumber;

    @NotNull(message = "Nominal withdraw tidak boleh kosong")
    @DecimalMin(value = "50000.00", message = "Minimal Withdraw adalah Rp 50.000")
    private BigDecimal amount;

    //Optional
    private String description;

}
