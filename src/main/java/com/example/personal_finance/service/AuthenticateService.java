package com.example.personal_finance.service;
import com.example.personal_finance.dto.users.*;
import org.springframework.http.ResponseEntity;

public interface AuthenticateService {

    /**
     * Register a new user.
     */
    ResponseEntity<String> register(RegisterRequest request);

    /**
     * Authenticate user and generate JWT tokens.
     */
    ResponseEntity<LoginResponse> login(LoginRequest request);

    /**
     * Refresh access token using a valid refresh token.
     */
    ResponseEntity<LoginResponse> refreshToken(RefreshTokenRequest request);

    /**
     * Update user password after verifying the old password.
     */
    ResponseEntity<String> updatePassword(String email, String oldPassword, String newPassword);

    /**
     * Reset user password: generate a access token to accept the next request in 5 mins
     */
    ResponseEntity<String> resetPassword(String email);

    /**
     * set user password: read email info from token
     */
    public ResponseEntity<String> setPassword(String email, String password);
}