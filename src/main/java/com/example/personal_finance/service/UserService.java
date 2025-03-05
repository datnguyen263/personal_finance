package com.example.personal_finance.service;
import com.example.personal_finance.dto.users.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    /**
     * Get user info
     */
    ResponseEntity<UserResponse> getUserProfile(String email);

    /**
     * Update user info
     */
    ResponseEntity<UserResponse> updateProfile(Long userId, UpdateProfileRequest request);
}