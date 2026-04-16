package com.Firmanann.CoreBankingSystem.auth.service;

import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

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
}
