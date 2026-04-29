package com.Firmanann.CoreBankingSystem.Auth;

import com.Firmanann.CoreBankingSystem.auth.dto.*;
import com.Firmanann.CoreBankingSystem.auth.service.AuthService;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository.RefreshTokenRepository;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.service.RefreshTokenService;
import com.Firmanann.CoreBankingSystem.global.jwt.service.JwtService;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetails;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.repository.UserRepository;
import com.Firmanann.CoreBankingSystem.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;



    @Test
    void register_ShouldReturnRegisterResponse_WhenSuccess(){

        //Arrange : Data & Dependency provider
        //Buat data request
        RegisterRequest request = new RegisterRequest("Firmanann", "firman@gmail.com", "manmanman");

        //Masukkan data ke entity
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("Firmanann");
        mockUser.setEmail("firman@gmail.com");
        mockUser.setPassword("manmanman");
        mockUser.setCreatedAt(LocalDateTime.now());

        //Berikan data untuk service yang berjalan
        when(userService.createUser(any())).thenReturn(mockUser);

        //Act
        //Jalankan logic
        RegisterResponse response = authService.register(request);

        //Assert
        //Hasl
        assertNotNull(response);
        assertEquals(mockUser.getId(), response.getId());
        assertEquals(mockUser.getUsername(), response.getUsername());
        assertEquals(mockUser.getEmail(), response.getEmail());

    }

    //Success Coondition
    @Test
    void login_shouldReturnLoginResponse_whenSuccess(){

        //Arrange data
        LoginRequest request = new LoginRequest("firman@gmail.com", "manmanman");

        //Buat mock data user
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("firman");
        mockUser.setEmail("firman@gmail.com");

        //Buat mock data token
        String mockAccessToken = "mock-access-token";
        String mockRefreshToken = "mock-refresh-token";
        Date mockExpriryDate = Date.from(Instant.now().plusSeconds(3600));

        //Matikan/Skip authentication manager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        //Proses jwt
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn(mockAccessToken);
        when(jwtService.generateRefreshToken(any(CustomUserDetails.class))).thenReturn(mockRefreshToken);
        when(jwtService.extractExpiration(mockRefreshToken)).thenReturn(mockExpriryDate);

        //Act
        LoginResponse response = authService.login(request);

        //Validasi hasil
        assertNotNull(response);
        assertEquals(mockAccessToken, response.getAccessToken());
        assertEquals(mockUser.getUsername(), response.getUsername());

        //Verify repository
        verify(refreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }


    @Test
    void refreshToken_shouldReturnToken_whenSuccess(){

        //Request = Refresh Token
        RefreshTokenRequest request = new RefreshTokenRequest("valid-old-token");

        //Membuat identitas token lama
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("firman");

        RefreshTokenEntity oldTokenEntity = new RefreshTokenEntity();
        oldTokenEntity.setToken("valid-old-token");
        oldTokenEntity.setUser(mockUser);
        oldTokenEntity.setExpiryDate(Instant.now().plusSeconds(3600));
        oldTokenEntity.setCreatedAt(Instant.now().minusSeconds(86400));

        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-Refresh-Token";

        //Mocking
        when(refreshTokenRepository.findByToken(request.getRefreshToken())).thenReturn(Optional.of(oldTokenEntity));
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn(newAccessToken);
        when(jwtService.generateRefreshToken(any(CustomUserDetails.class))).thenReturn(newRefreshToken);

        //Act
        RefreshTokenResponse response = authService.refreshToken(request);

        //Assert
        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(newRefreshToken, response.getRefreshToken());

        //Verifikasi
        verify(refreshTokenRepository, times(1)).delete(oldTokenEntity);
        verify(refreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }

    @Test
    void logout_success_shouldDeleteTokenAndClearContext(){

        //Siapkan data
        String requestToken = "valid-token";
        LogoutRequest request = new LogoutRequest(requestToken);
        String username = "firman";


        RefreshTokenEntity mockTokenEntity = new RefreshTokenEntity();

        //Set up mocking
        when(jwtService.extractUsername(requestToken)).thenReturn(username);

        //Manipulation authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        //Buat contect palsu ke security context holder
        SecurityContextHolder.setContext(securityContext);

        //Mock database
        when(refreshTokenRepository.findByToken(requestToken)).thenReturn(Optional.of(mockTokenEntity));

        //act/Eksekusi
        authService.logout(request);

        //Validasi kebenaran token dihapus dari db
        verify(refreshTokenRepository, times(1)).delete(mockTokenEntity);

        //Memastikan security contect holder di hapus
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
