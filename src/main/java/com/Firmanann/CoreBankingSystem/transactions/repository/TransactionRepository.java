package com.Firmanann.CoreBankingSystem.transactions.repository;

import com.Firmanann.CoreBankingSystem.transactions.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}
