package com.Firmanann.CoreBankingSystem.Auth;

import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.auth.service.AuthService;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

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
}
