package com.Firmanann.CoreBankingSystem.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;

    @JsonProperty("created_at")
    private String createdAt;
}
