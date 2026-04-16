package com.Firmanann.CoreBankingSystem.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//Blueprint data request ~ Validation ~ input error message
@Data
public class RegisterRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 5, max = 50)
    private String username;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email (message = "EMAIL_FORMAT")
    private String email;

    @NotBlank (message = "PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_SIZE")
    private String password;
}
