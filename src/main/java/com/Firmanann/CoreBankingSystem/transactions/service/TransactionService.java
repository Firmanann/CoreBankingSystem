package com.Firmanann.CoreBankingSystem.transactions.service;

import com.Firmanann.CoreBankingSystem.accounts.entity.AccountEntity;
import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
import com.Firmanann.CoreBankingSystem.accounts.repository.AccountsRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import com.Firmanann.CoreBankingSystem.transactions.dto.DepositRequest;
import com.Firmanann.CoreBankingSystem.transactions.dto.DepositResponse;
import com.Firmanann.CoreBankingSystem.transactions.dto.WithdrawRequest;
import com.Firmanann.CoreBankingSystem.transactions.dto.WithdrawResponse;
import com.Firmanann.CoreBankingSystem.transactions.entity.TransactionEntity;
import com.Firmanann.CoreBankingSystem.transactions.entity.TransactionStatus;
import com.Firmanann.CoreBankingSystem.transactions.entity.TransactionType;
import com.Firmanann.CoreBankingSystem.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    //Inject Beans
    private final AccountsRepository accountsRepository;
    private final TransactionRepository transactionRepository;

    //Generate Reference number
    private String generateReferenceNumber(String prefix) {
        //Ambil tanggal hari ini (Format: YYYYMMDD)
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //Potong 6 karakter acak dari UUID dan jadikan huruf besar semua
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        //Gabungkan semuanya
        return prefix + "-" + datePart + "-" + randomPart;
    }

    @Transactional
    public DepositResponse deposit (DepositRequest request){

        //Get data target User
        AccountEntity targetUser = accountsRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        //Validate status
        if(targetUser.getStatus() != AccountStatus.ACTIVE){
            throw new BusinessException(ErrorCode.ACCOUNT_STATUS_INACTIVE);
        }

        //Calculate final balance
        BigDecimal finalBalance = targetUser.getBalance().add(request.getAmount());

        //Change final balance of target user
        targetUser.setBalance(finalBalance);

        //Save changes
        AccountEntity saveBalance = accountsRepository.save(targetUser);

        //Create new Transaction Object
        TransactionEntity transactionData = TransactionEntity.builder()
                .referenceNumber(generateReferenceNumber("DEP"))
                .sourceAccountNumber("CASH_DEPOSIT")
                .targetAccountNumber(request.getTargetAccountNumber())
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        //save Object
        TransactionEntity newTransaction = transactionRepository.save(transactionData);


        //Design response
        return DepositResponse.builder()
                .referenceNumber(newTransaction.getReferenceNumber())
                .targetAccountNumber(newTransaction.getTargetAccountNumber())
                .amount(newTransaction.getAmount())
                .currentBalance(targetUser.getBalance())
                .timestamp(newTransaction.getCreatedAt())
                .build();
    }

    @Transactional
    public WithdrawResponse withdraw(WithdrawRequest request) {

        //Validate existing user
        AccountEntity user = accountsRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

        //Validate account status
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ACCOUNT_STATUS_INACTIVE);
        }

        //Validate balance
        if (request.getAmount().compareTo(user.getBalance()) > 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        //Calculate final balance
        BigDecimal finalBalance = user.getBalance().subtract(request.getAmount());

        //Save changes balance
        user.setBalance(finalBalance);
        accountsRepository.save(user);

        //Create new Transaction Object
        TransactionEntity transactionData = TransactionEntity.builder()
                .referenceNumber(generateReferenceNumber("WITH"))
                .sourceAccountNumber(request.getSourceAccountNumber())
                .targetAccountNumber("CASH_WITHDRAWAL")
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .transactionStatus(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        //Save Object
        TransactionEntity newTransaction = transactionRepository.save(transactionData);

        //Design response
        return WithdrawResponse.builder()
                .amount(request.getAmount())
                .currentBalance(finalBalance)
                .referenceNumber(newTransaction.getReferenceNumber())
                .sourceAccountNumber(request.getSourceAccountNumber())
                .timestamp(newTransaction.getCreatedAt())
                .build();
    }
}
