package com.Firmanann.CoreBankingSystem.transactions.controller;


import com.Firmanann.CoreBankingSystem.global.response.GlobalResponse;
import com.Firmanann.CoreBankingSystem.transactions.dto.DepositRequest;
import com.Firmanann.CoreBankingSystem.transactions.dto.DepositResponse;
import com.Firmanann.CoreBankingSystem.transactions.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<GlobalResponse<DepositResponse>> deposit (DepositRequest request){

        //Process
        DepositResponse data = transactionService.deposit(request);

        //design response
        GlobalResponse<DepositResponse> response = GlobalResponse.<DepositResponse>builder()
                .status("success")
                .message("Patch Successfuly")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
