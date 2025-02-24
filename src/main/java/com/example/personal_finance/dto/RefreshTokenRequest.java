package com.example.personal_finance.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
