package com.Firmanann.CoreBankingSystem.Auth;

import com.Firmanann.CoreBankingSystem.auth.dto.LoginRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.LoginResponse;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.auth.service.AuthService;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.Firmanann.CoreBankingSystem.global.jwt.refreshtoken.repository.RefreshTokenRepository;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        mockUser.setPassword("manmanman");

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
        assertEquals("firman", response.getUsername());

        //Verify repository
        verify(refreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
    }
}
