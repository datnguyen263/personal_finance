package com.example.personal_finance.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
