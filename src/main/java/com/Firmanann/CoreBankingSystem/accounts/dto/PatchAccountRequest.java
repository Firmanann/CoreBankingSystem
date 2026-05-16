package com.Firmanann.CoreBankingSystem.accounts.dto;

import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
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
