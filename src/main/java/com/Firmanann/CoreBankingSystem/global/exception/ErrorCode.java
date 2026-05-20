package com.Firmanann.CoreBankingSystem.global.exception;

import org.springframework.http.HttpStatus;

//Base error message
public enum ErrorCode {

    // Input/Output error message (Kesalahan validasi dari sisi Client)
    USERNAME_REQUIRED(HttpStatus.BAD_REQUEST, "Nama wajib diisi"),
    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "Email wajib diisi"),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "Password wajib diisi"),
    EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "Email format not valid"),
    PASSWORD_SIZE(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters"),

    // Business error message
    EMAIL_EXISTS(HttpStatus.CONFLICT, "Email sudah digunakan"), // 409 Conflict standar untuk duplikasi data
    ROLE_MISSING(HttpStatus.BAD_REQUEST, "Role input tidak ditemukan"),
    EMAIL_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "Email atau Password salah"), // 401 Unauthorized untuk gagal login
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User tidak ditemukan"), // 404 Not Found untuk data kosong di DB
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Roles tidak ditemukan"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token Sudah Expire"),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Refresh Token Invalid"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "Data user Invalid"),

    // Account module business
    ACCOUNT_EXISTS(HttpStatus.CONFLICT, "Pembuatan Rekening gagal : Anda Sudah memiliki rekening aktif"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account Not Found"),
    STATUS_REQUIRED(HttpStatus.BAD_REQUEST,"Status dibutuhkan"),
    ACCOUNT_STATUS_INACTIVE(HttpStatus.UNAUTHORIZED, "Rekening Tidak Aktif"),
    INSUFFICIENT_BALANCE(HttpStatus.PAYMENT_REQUIRED, "Saldo tidak mencukupi"),
    SAME_ACCOUNT_TRANSFER(HttpStatus.UNPROCESSABLE_ENTITY, "Rekening tujuan tidak boleh sama dengan rekening pengirim.");



    private final HttpStatus httpStatus;
    private final String message;

    // Constructor
    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // Getter untuk HTTP Status
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // Getter untuk Message
    public String getMessage() {
        return message;
    }
}