package com.example.personal_finance.service;

import com.example.personal_finance.dto.LoginRequest;
import com.example.personal_finance.dto.LoginResponse;
import com.example.personal_finance.dto.RegisterRequest;
import com.example.personal_finance.dto.RefreshTokenRequest;
import com.example.personal_finance.model.User;
import com.example.personal_finance.repository.UserRepository;
import com.example.personal_finance.security.JwtUtil;
import com.example.personal_finance.security.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoderUtil passwordEncoderUtil, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Error: Passwords do not match";
        }
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "User already exists with this email";
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
        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean passwordMatches = passwordEncoderUtil.verifyPassword(request.getPassword(), user.getPasswordHash());

            if (passwordMatches) {
                String accessToken = jwtUtil.generateAccessToken(request.getEmail());
                String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

                return new LoginResponse(accessToken, refreshToken, "Bearer", 3600);
            }
        }
        throw new RuntimeException("Invalid email or password");
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (jwtUtil.validateToken(request.getRefreshToken())) {
            String email = jwtUtil.extractEmail(request.getRefreshToken());
            String newAccessToken = jwtUtil.generateAccessToken(email);
            return new LoginResponse(newAccessToken, request.getRefreshToken(), "Bearer", 3600);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public String resetPassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();

        // Verify old password
        if (!passwordEncoderUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
            return "Old password is incorrect";
        }

        // Generate new hashed password
        String salt = passwordEncoderUtil.generateSalt();
        String hashedNewPassword = passwordEncoderUtil.hashPassword(newPassword, salt);

        user.setSalt(salt);
        user.setPasswordHash(hashedNewPassword);
        userRepository.save(user);

        return "Password updated successfully";
    }
}
