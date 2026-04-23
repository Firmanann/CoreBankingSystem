package com.Firmanann.CoreBankingSystem.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_FORMAT")
    private String email;

    @NotBlank (message = "PASSWORD_REQUIRED")
    private String password;
}
