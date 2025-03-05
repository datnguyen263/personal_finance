package com.example.personal_finance.service.impl;

import com.example.personal_finance.dto.users.UpdateProfileRequest;
import com.example.personal_finance.dto.users.UserResponse;
import com.example.personal_finance.model.User;
import com.example.personal_finance.repository.UserRepository;
import com.example.personal_finance.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get user info
     */
    @Override
    public ResponseEntity<UserResponse> getUserProfile(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();
        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Update user info
     */
    @Override
    public ResponseEntity<UserResponse> updateProfile(Long userId, UpdateProfileRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOptional.get();

        // Update fields
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBalance(request.getBalance());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        userRepository.save(user);
        return ResponseEntity.ok(response);
    }
}