package com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.service;

import com.Firmanann.CoreBankingSystem.auth.dto.RefreshTokenRequest;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    public void verifyTotalSession(RefreshTokenEntity token) {
        // 1. Tentukan batas maksimal (misal 7 hari)
        long maxSessionDays = 7;

        // 2. Ambil waktu sekarang & waktu pertama kali login (createdAt)
        Instant now = Instant.now();
        Instant limit = token.getCreatedAt().plus(maxSessionDays, ChronoUnit.DAYS);

        // 3. Bandingkan
        if (now.isAfter(limit)) {
            // Jika sudah lewat 7 hari, hapus token dan usir user (Force Logout)
            refreshTokenRepository.delete(token);
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }
}
