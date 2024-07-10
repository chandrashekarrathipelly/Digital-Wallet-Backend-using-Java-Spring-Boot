package com.example.DigitalWallet.AuthResponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private String token;
}
