package com.Firmanann.CoreBankingSystem.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // input exception handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        // Loop to take all error field
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorKey = error.getDefaultMessage();

            try {
                // Translate message from ErrorCode
                ErrorCode errorCode = ErrorCode.valueOf(errorKey);
                fieldErrors.put(fieldName, errorCode.getMessage());
            } catch (IllegalArgumentException e) {
                // Jaring pengaman: Kalau message bukan dari Enum, pakai message aslinya aja
                fieldErrors.put(fieldName, errorKey);
            }
        });

        // Design format
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("data", null);
        response.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Business exception handler
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {

        // 1. Buka koper ErrorCode dan ambil HTTP Status-nya secara dinamis
        HttpStatus status = ex.getErrorCode().getHttpStatus();

        // 2. Design format
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("message", ex.getMessage());
        response.put("data", null);
        response.put("errors", null);

        // 3. Return menggunakan status yang sesuai konteks
        return ResponseEntity.status(status).body(response);
    }
}