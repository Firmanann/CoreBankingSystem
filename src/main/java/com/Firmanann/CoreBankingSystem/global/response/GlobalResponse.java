package com.Firmanann.CoreBankingSystem.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse<T> {

    private String status;
    private String message;
    private T data;
    private Map<String, String> errors;
}
