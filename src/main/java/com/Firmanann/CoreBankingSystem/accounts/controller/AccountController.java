package com.Firmanann.CoreBankingSystem.accounts.controller;


import com.Firmanann.CoreBankingSystem.accounts.dto.CreateAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.dto.AccountDetailsResponse;
import com.Firmanann.CoreBankingSystem.accounts.dto.PatchAccountRequest;
import com.Firmanann.CoreBankingSystem.accounts.dto.PatchAccountResponse;
import com.Firmanann.CoreBankingSystem.accounts.service.AccountService;
import com.Firmanann.CoreBankingSystem.global.jwt.userDetails.CustomUserDetails;
import com.Firmanann.CoreBankingSystem.global.response.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    //inject bean
    private final AccountService accountService;

    //Constructor
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }


    @PostMapping("/me")
    public ResponseEntity<GlobalResponse<CreateAccountResponse>> createAccount (@AuthenticationPrincipal CustomUserDetails currentUserDetails){

        //1 and 2 logic step (Create Account)
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

    //Account Details Account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<GlobalResponse<AccountDetailsResponse>> getAccountDetail(@PathVariable String accountNumber, @AuthenticationPrincipal CustomUserDetails currentUserDetails){

        //Take data user form token
        Long loggedInUserId = currentUserDetails.getId();

        //send data user to service
        AccountDetailsResponse data = accountService.getAccountDetails(accountNumber, loggedInUserId);

        //design response
        GlobalResponse<AccountDetailsResponse> response = GlobalResponse.<AccountDetailsResponse>builder()
                .status("success")
                .message("Account Data")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //
    @PatchMapping("/{accountNumber}")
    public ResponseEntity<GlobalResponse<PatchAccountResponse>> patchAccount (@Valid @RequestBody PatchAccountRequest request, @PathVariable String accountNumber, @AuthenticationPrincipal CustomUserDetails currentUserDetails){

        //Take data user form token
        Long loggedInUserId = currentUserDetails.getId();

        //process
        PatchAccountResponse data = accountService.patchAccount(accountNumber, loggedInUserId, request);

        //design response
        GlobalResponse<PatchAccountResponse> response = GlobalResponse.<PatchAccountResponse>builder()
                .status("success")
                .message("Patch Successfuly")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
