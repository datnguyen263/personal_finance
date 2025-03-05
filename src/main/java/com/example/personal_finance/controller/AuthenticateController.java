package com.example.personal_finance.controller;

import com.example.personal_finance.dto.users.*;
import com.example.personal_finance.service.AuthenticateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/auth")
public class AuthenticateController {

    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return authenticateService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authenticateService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authenticateService.refreshToken(request);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam String email,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return authenticateService.updatePassword(email, oldPassword, newPassword);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email) {
        return authenticateService.resetPassword(email);
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String password) {
        return authenticateService.setPassword(userDetails.getUsername(), password);
    }
}