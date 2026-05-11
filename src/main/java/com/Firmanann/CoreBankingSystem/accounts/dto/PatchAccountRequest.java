package com.Firmanann.CoreBankingSystem.accounts.dto;

import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchAccountRequest {

    @NotNull(message = "STATUS_REQUIRED")
    private AccountStatus status;
}
