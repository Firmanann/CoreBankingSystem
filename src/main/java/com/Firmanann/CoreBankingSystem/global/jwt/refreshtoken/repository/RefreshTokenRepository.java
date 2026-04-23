package com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository;

import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // Dipakai nanti saat user minta token baru
    Optional<RefreshTokenEntity> findByToken(String token);

    // Dipakai saat user klik "Logout"
    void deleteByUser(UserEntity user);
}
