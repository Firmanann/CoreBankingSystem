package com.Firmanann.CoreBankingSystem.user.repository;

import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //Query untuk mengecek ketersediaan email
    boolean existsByEmail(String email);
}
