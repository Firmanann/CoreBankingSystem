package com.Firmanann.CoreBankingSystem.accounts.service;


import com.Firmanann.CoreBankingSystem.accounts.dto.CreateAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.entity.AccountEntity;
import com.Firmanann.CoreBankingSystem.accounts.repository.AccountsRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountsRepository accountsRepository;
    private final UserRepository userRepository;

    //generate create account
    private String generateUniqueAccountNumber(){
        SecureRandom secureRandom = new SecureRandom();
        String accountNumber;

        do {

            int randomNumber = secureRandom.nextInt(100_000_000);
            accountNumber = "123" + String.format("%08d", randomNumber);
        } while (accountsRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    @Transactional
    public CreateAccountResponse createAccount (Long loggedInUserId){


        //3. Validate account status
        boolean hasActiveAccount = accountsRepository.existsByUserIdAndStatus(loggedInUserId, AccountStatus.ACTIVE);

        if (hasActiveAccount){
            throw new BusinessException(ErrorCode.ACCOUNT_EXISTS);
        }

        //Validate and get data user
        UserEntity user = userRepository.findById(loggedInUserId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));


        //4.Validate existing account number and generate new account number
        String accountNumber = generateUniqueAccountNumber();

        //5.Mapping to Account entity
        AccountEntity newAccount = new AccountEntity();
        newAccount.setUser(user);
        newAccount.setAccountNumber(accountNumber);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setCreatedAt(Instant.now());

        //Save by Repositoy
        accountsRepository.save(newAccount);

        return CreateAccountResponse.builder().
                accountNumber(accountNumber).
                balance(newAccount.getBalance()).
                status(newAccount.getStatus()).
                createdAt(newAccount.getCreatedAt()).
                build();
    }
}
