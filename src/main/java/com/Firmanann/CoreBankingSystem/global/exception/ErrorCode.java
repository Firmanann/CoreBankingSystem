package com.Firmanann.CoreBankingSystem.global.exception;

//Base error message
public enum ErrorCode {

    //Input/Output error message
    USERNAME_REQUIRED("Nama wajib diisi"),
    EMAIL_REQUIRED("Email wajib diisi"),
    PASSWORD_REQUIRED("Password wajib diisi"),
    EMAIL_FORMAT("Email format not valid"),
    PASSWORD_SIZE("Password must be at least 8 characters"),

    //Business error message
    EMAIL_EXISTS("Email sudah digunakan"),
    ROLE_MISSING("Role tidak ditemukan"),
    EMAIL_PASSWORD_INVALID("Email atau Password salah"),
    USER_NOT_FOUND("User tidak ditemukan"),
    ROLE_NOT_FOUND("Roles tidak ditemukan"),
    REFRESH_TOKEN_EXPIRED("Refresh Token Sudah Expire"),
    REFRESH_TOKEN_INVALID("Refresh Token Invalid"),
    UNAUTHORIZED_LOGOUT("Data user Invalid");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }



}
