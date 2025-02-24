package com.example.personal_finance.controller;

import com.example.personal_finance.dto.LoginRequest;
import com.example.personal_finance.dto.LoginResponse;
import com.example.personal_finance.dto.RegisterRequest;
import com.example.personal_finance.dto.RefreshTokenRequest;
import com.example.personal_finance.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return authService.resetPassword(email, oldPassword, newPassword);
    }
}
