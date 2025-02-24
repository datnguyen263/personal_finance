package com.example.personal_finance.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {

    public String generateSalt() {
        return BCrypt.gensalt();
    }

    public String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
