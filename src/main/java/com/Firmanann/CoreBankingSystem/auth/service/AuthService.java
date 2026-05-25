package com.Firmanann.CoreBankingSystem.auth.service;

import com.Firmanann.CoreBankingSystem.auth.dto.*;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository.RefreshTokenRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.service.RefreshTokenService;
import com.Firmanann.CoreBankingSystem.global.jwt.service.JwtService;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetails;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.repository.UserRepository;
import com.Firmanann.CoreBankingSystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.Firmanann.CoreBankingSystem.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    public RegisterResponse register(RegisterRequest request){

        //Call userservice and put to the new object
        UserEntity newUser = userService.createUser(request);

        //Mapping userEntity to RegisterResponse (Designing Success Output)
        return RegisterResponse.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .createdAt(newUser.getCreatedAt().toString())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        //1. Ambil data user dari database
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(EMAIL_NOT_FOUND));

        try {
            //2. Validate email & password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            // Tangkap error dari Spring Security dan ubah jadi error bisnis lu
            throw new BusinessException(EMAIL_PASSWORD_INVALID);
        }

        // 3. Bungkus data user ke tempat CustomUserDetails
        CustomUserDetails customUser = new CustomUserDetails(user);

        // 4. Generate token
        String accessToken = jwtService.generateToken(customUser);
        String refreshToken = jwtService.generateRefreshToken(customUser);

        // 5. Extract tanggal hangus token
        Instant expiryDate = jwtService.extractExpiration(refreshToken).toInstant();

        //Desain data token/
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(expiryDate)
                .createdAt(Instant.now())
                .build();

        //6. Simpan Data Refresh token ke db
        refreshTokenRepository.save(refreshTokenEntity);

        // 7. Susun dan kembalikan data Response-nya ke Controller
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    public RefreshTokenResponse refreshToken (RefreshTokenRequest request){

        //Functional programming
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map( token ->{

                    //1. Cek expire date.
                    if (token.getExpiryDate().isBefore(Instant.now())){
                        //Jika true hapus
                        refreshTokenRepository.delete(token);
                        throw new BusinessException(REFRESH_TOKEN_EXPIRED);
                    }
                    return token;
                })
                .map(token -> {

                    //1. Validasi apakah umur token > 7 hari
                    refreshTokenService.verifyTotalSession(token);

                    //2. Hapus token yang lama
                    refreshTokenRepository.delete(token);

                    //3. Ambil data user dari token
                    UserEntity user = token.getUser();

                    //4.Masukkan data user ke custom user details
                    CustomUserDetails detailUser = new CustomUserDetails(user);

                    //5. Generate token baru
                    String accessToken = jwtService.generateToken(detailUser);
                    String refreshToken = jwtService.generateRefreshToken(detailUser);

                    //6. Ambil createdAt dari token yang lama sebelum dihapus
                    Instant originalCreationDate = token.getCreatedAt();

                    //7. Build data token
                    RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                            .token(refreshToken)
                            .user(user)
                            .expiryDate(Instant.now())
                            .createdAt(originalCreationDate)
                            .build();

                    //8. Simpan data token ke db
                    refreshTokenRepository.save(refreshTokenEntity);

                    return RefreshTokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                })
        .orElseThrow(() -> new BusinessException(REFRESH_TOKEN_INVALID));
    }

    public void logout (LogoutRequest request){

        //Validasi token request
        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            throw new BusinessException(REFRESH_TOKEN_INVALID);
        }

        //1. Ambil data user dari token request
        String userToken = jwtService.extractUsername(request.getRefreshToken());

        //2. Ambil data user yang sedang aktif/login
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        //3. Verifikasi data user token == data user aktif
        if (!userToken.equals(currentUser)) {
            throw new BusinessException(UNAUTHORIZED_USER);
        }

        //4. Hapus data refresh token di db
        refreshTokenRepository.findByToken(request.getRefreshToken())
                .ifPresent(token -> refreshTokenRepository.delete(token));

        //5. Bersihkan data di securityContextHolder
        SecurityContextHolder.clearContext();
    }

}