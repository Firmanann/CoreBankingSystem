package com.Firmanann.CoreBankingSystem.auth.controller;

import com.Firmanann.CoreBankingSystem.auth.dto.*;
import com.Firmanann.CoreBankingSystem.auth.service.AuthService;
import com.Firmanann.CoreBankingSystem.global.response.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController (AuthService authService){
        this.authService = authService;
    }


    //catch register endpoint
    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<RegisterResponse>> register (@Valid @RequestBody RegisterRequest request){

        RegisterResponse data = authService.register(request);

        GlobalResponse<RegisterResponse> response = GlobalResponse.<RegisterResponse>builder()
                .status("success")
                .message("User registered successfully")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Catch login endpoint
    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login (@Valid @RequestBody LoginRequest request){

        LoginResponse data = authService.login(request);

        GlobalResponse<LoginResponse> response = GlobalResponse.<LoginResponse>builder()
                .status("success")
                .message("login Successfully")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Catch refresh token endpoint
    @PostMapping("/refresh")
    public ResponseEntity<GlobalResponse<RefreshTokenResponse>> refreshToken (@Valid @RequestBody RefreshTokenRequest request) {

        //Panggil refresh token service
        RefreshTokenResponse data = authService.refreshToken(request);

        GlobalResponse<RefreshTokenResponse> response = GlobalResponse.<RefreshTokenResponse>builder()
                .status("success")
                .message("Token refreshed successfully")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Catch Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponse> logout (@Valid @RequestBody LogoutRequest request){

        authService.logout(request);

        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .message("Logout Success")
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


