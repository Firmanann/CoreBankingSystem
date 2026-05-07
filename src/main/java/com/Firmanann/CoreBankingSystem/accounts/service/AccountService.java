package com.Firmanann.CoreBankingSystem.accounts.service;


import com.Firmanann.CoreBankingSystem.accounts.dto.CreateAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.repository.AccountsRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.AccountStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountsRepository accountsRepository;



    /*
    @Transactional
    public CreateAccountResponse createAccount (Long loggedInUserId){


        //1. Validate
        boolean hasActiveAccount = accountsRepository.existsByUserIdAndStatus(loggedInUserId, AccountStatus.ACTIVE);

        if (hasActiveAccount){
            throw new BusinessException(ErrorCode.ACCOUNT_EXISTS);
        }



    }
     */
}
