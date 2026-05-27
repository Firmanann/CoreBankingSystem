package com.Firmanann.CoreBankingSystem.transactions.controller;


import com.Firmanann.CoreBankingSystem.global.response.GlobalResponse;
import com.Firmanann.CoreBankingSystem.transactions.dto.*;
import com.Firmanann.CoreBankingSystem.transactions.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    //Inject Bean
    private final TransactionService transactionService;

    //Constructor
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<GlobalResponse<DepositResponse>> deposit (@Valid @RequestBody DepositRequest request){

        //Process
        DepositResponse data = transactionService.deposit(request);

        //design response
        GlobalResponse<DepositResponse> response = GlobalResponse.<DepositResponse>builder()
                .status("success")
                .message("Deposit Successfuly")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<GlobalResponse<WithdrawResponse>> withdraw (@Valid @RequestBody WithdrawRequest request){

        WithdrawResponse data = transactionService.withdraw(request);

        GlobalResponse<WithdrawResponse> response = GlobalResponse.<WithdrawResponse>builder()
                .status("success")
                .message("Withdraw Successfuly")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<GlobalResponse<TransferResponse>> transfer (@Valid @RequestBody TransferRequest request){

        //Process
        TransferResponse data = transactionService.transfer(request);

        //Response desgin
        GlobalResponse<TransferResponse> response = GlobalResponse.<TransferResponse>builder()
                .status("success")
                .message("Transfer Successfuly")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
