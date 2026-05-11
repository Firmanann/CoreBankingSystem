package com.Firmanann.CoreBankingSystem.accounts.service;


import com.Firmanann.CoreBankingSystem.accounts.dto.AccountDetailsResponse;
import com.Firmanann.CoreBankingSystem.accounts.dto.CreateAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.dto.PatchAccountRequest;
import com.Firmanann.CoreBankingSystem.accounts.dto.PatchAccountResponse;
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


        //Validate account status
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


    public AccountDetailsResponse getAccountDetails(String accountNumber, Long loggedInUserId){

        //Get account data from db
        AccountEntity accountData = accountsRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        //get user data by account db
        if (!accountData.getUser().getId().equals(loggedInUserId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }

        //design response
        return AccountDetailsResponse.builder()
                .username(accountData.getUser().getUsername())
                .accountNumber(accountData.getAccountNumber())
                .balance(accountData.getBalance())
                .build();
    }

    public PatchAccountResponse patchAccount (String accountNumber, Long loggedInUserId, PatchAccountRequest request){

        //Validate and get account data
        AccountEntity accountData = accountsRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        //validate user data by account db
        if (!accountData.getUser().getId().equals(loggedInUserId)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }

        //Change Account status
        accountData.setStatus(request.getStatus());

        //save data
        AccountEntity saveChanges = accountsRepository.save(accountData);

        //Design response
        return PatchAccountResponse.builder()
                .status(saveChanges.getStatus())
                .build();
    }
}

