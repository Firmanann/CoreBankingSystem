package com.Firmanann.CoreBankingSystem.accounts.controller;


import com.Firmanann.CoreBankingSystem.accounts.dto.CreateAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.service.AccountService;
import com.Firmanann.CoreBankingSystem.auth.dto.RegisterResponse;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetails;
import com.Firmanann.CoreBankingSystem.global.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    //inject bean
    private final AccountService accountService;

    //Constructor
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    /*
    @PostMapping("/me")
    public ResponseEntity<GlobalResponse<CreateAccountResponse>> createAccount (@AuthenticationPrincipal CustomUserDetails currentUserDetails){

        //Take userid from token who's login
        Long loggedInUserId = currentUserDetails.getId();

        //Send data user to processed in service
        CreateAccountResponse data = accountService.createAccount(loggedInUserId);

        //Design Response
        GlobalResponse<CreateAccountResponse> response = GlobalResponse.<CreateAccountResponse>builder()
                .status("success")
                .message("Account Created Successfully")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
     */
}
