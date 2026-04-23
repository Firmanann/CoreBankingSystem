package com.Firmanann.CoreBankingSystem.auth.service;

import com.Firmanann.CoreBankingSystem.auth.dto.LoginRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.LoginResponse;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository.RefreshTokenRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.jwt.service.JwtService;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetails;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.repository.UserRepository;
import com.Firmanann.CoreBankingSystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.Firmanann.CoreBankingSystem.global.exception.ErrorCode.EMAIL_PASSWORD_INVALID;
import static com.Firmanann.CoreBankingSystem.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

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

        try {
            // 1. Serahkan urusan cek Email & Password ke Satpam Utama (AuthenticationManager)
            // Ini akan otomatis memanggil CustomUserDetailsService dan BCrypt lu.
            // Kalau password salah, dia otomatis ngelempar error.
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

        // 2. Ambil data user dari database
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        // 3. Bungkus data user ke tempat CustomUserDetails
        CustomUserDetails customUser = new CustomUserDetails(user);

        // 4. Generate token
        String accessToken = jwtService.generateToken(customUser);
        String refreshToken = jwtService.generateRefreshToken(customUser);

        // 5. Extract tanggal hangus token
        Instant expiryDate = jwtService.extractExpiration(refreshToken).toInstant();

        //Desain datanya
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(expiryDate)
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
}