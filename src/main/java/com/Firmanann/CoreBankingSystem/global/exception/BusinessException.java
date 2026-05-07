package com.Firmanann.CoreBankingSystem.global.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // Sekarang cuma butuh 1 parameter: ErrorCode
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}