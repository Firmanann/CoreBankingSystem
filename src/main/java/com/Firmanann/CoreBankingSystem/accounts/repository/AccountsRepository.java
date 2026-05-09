package com.Firmanann.CoreBankingSystem.accounts.repository;

import com.Firmanann.CoreBankingSystem.accounts.entity.AccountEntity;
import com.Firmanann.CoreBankingSystem.accounts.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<AccountEntity, Long> {


    //Search data by account number
    Optional<AccountEntity> findByAccountNumber (String accountNumber);

    //method to check status by user id
    boolean existsByUserIdAndStatus(Long userId, AccountStatus status);

    //method to check existing account number
    boolean existsByAccountNumber(String accountNumber);

}
