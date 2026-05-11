package com.Firmanann.CoreBankingSystem.accounts.dto;


import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PatchAccountResponse {

    private AccountStatus status;

}
