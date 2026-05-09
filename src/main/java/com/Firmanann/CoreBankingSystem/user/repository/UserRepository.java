package com.Firmanann.CoreBankingSystem.user.repository;

import com.Firmanann.CoreBankingSystem.roles.entity.RoleEntity;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //Query untuk mengecek ketersediaan email
    boolean existsByEmail(String email);

    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByEmail(String email);
}
