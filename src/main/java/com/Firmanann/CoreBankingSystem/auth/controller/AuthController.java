package com.Firmanann.CoreBankingSystem.auth.controller;

import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.auth.service.AuthService;
import com.Firmanann.CoreBankingSystem.common.response.GlobalResponse;
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


}
