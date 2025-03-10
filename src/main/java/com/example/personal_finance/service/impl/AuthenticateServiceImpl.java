package com.example.personal_finance.service.impl;

import com.example.personal_finance.dto.users.LoginRequest;
import com.example.personal_finance.dto.users.LoginResponse;
import com.example.personal_finance.dto.users.RefreshTokenRequest;
import com.example.personal_finance.dto.users.RegisterRequest;
import com.example.personal_finance.model.User;
import com.example.personal_finance.repository.UserRepository;
import com.example.personal_finance.security.JwtUtil;
import com.example.personal_finance.security.PasswordEncoderUtil;
import com.example.personal_finance.service.AuthenticateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtUtil jwtUtil;

    public AuthenticateServiceImpl(UserRepository userRepository, PasswordEncoderUtil passwordEncoderUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user.
     */
    @Override
    public ResponseEntity<String> register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Passwords do not match");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: User already exists with this email");
        }

        String salt = passwordEncoderUtil.generateSalt();
        String hashedPassword = passwordEncoderUtil.hashPassword(request.getPassword(), salt);

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .balance(0.0)
                .salt(salt)
                .passwordHash(hashedPassword)
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    /**
     * Authenticate user and generate JWT tokens.
     */
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Invalid email or password",0));
        }

        User user = userOptional.get();
        boolean passwordMatches = passwordEncoderUtil.verifyPassword(request.getPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Invalid email or password", 0));
        }

        String accessToken = jwtUtil.generateAccessToken(request.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, "Bearer", "SUCCEED", 3600));
    }

    /**
     * Refresh access token using a valid refresh token.
     */
    @Override
    public ResponseEntity<LoginResponse> refreshToken(RefreshTokenRequest request) {
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Invalid or expired refresh token", 0));
        }

        String email = jwtUtil.extractEmail(request.getRefreshToken());
        String newAccessToken = jwtUtil.generateAccessToken(email);

        return ResponseEntity.ok(new LoginResponse(newAccessToken, request.getRefreshToken(), "Bearer", "SUCCEED", 3600));
    }

    /**
     * Update user password after verifying the old password.
     */
    @Override
    public ResponseEntity<String> updatePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: User not found");
        }

        User user = userOptional.get();

        // Verify old password
        if (!passwordEncoderUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Old password is incorrect");
        }

        // Generate new hashed password
        String salt = passwordEncoderUtil.generateSalt();
        String hashedNewPassword = passwordEncoderUtil.hashPassword(newPassword, salt);

        user.setSalt(salt);
        user.setPasswordHash(hashedNewPassword);
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    /**
     * Reset user password: generate a access token to accept the next request in 5 mins
     */
    @Override
    public ResponseEntity<String> resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: User not found");
        }
        String accessToken = jwtUtil.generateAccessToken(email);
        return ResponseEntity.ok(accessToken);
    }

    /**
     * set user password: read email info from token
     */
    @Override
    public ResponseEntity<String> setPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: User not found");
        }

        User user = userOptional.get();
        // Generate new hashed password
        String salt = passwordEncoderUtil.generateSalt();
        String hashedNewPassword = passwordEncoderUtil.hashPassword(password, salt);

        user.setSalt(salt);
        user.setPasswordHash(hashedNewPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }
}