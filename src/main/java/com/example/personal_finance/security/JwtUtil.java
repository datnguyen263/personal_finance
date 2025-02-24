package com.example.personal_finance.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your-very-secure-secret-key-your-very-secure-secret-key"; // Ensure it's at least 256 bits
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;  // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;  // 7 days
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Initialize the key once
    }

    // Generate Access Token
    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate Refresh Token
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
            }
            return Jwts.parserBuilder()
                    .setSigningKey(key) // Ensure the key is properly set
                    .build()
                    .parseClaimsJws(token.trim()) // Trim any spaces
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token is expired: " + e.getMessage());
            throw new RuntimeException("Token expired");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            throw new RuntimeException("Invalid token");
        } catch (IllegalArgumentException e) {
            System.out.println("Token cannot be null or empty: " + e.getMessage());
            throw new RuntimeException("Token is empty");
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            throw new RuntimeException("Token parsing failed");
        }
    }


    // Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (MalformedJwtException | SecurityException e) {
            System.out.println("Invalid token");
        }
        return false;
    }
}
