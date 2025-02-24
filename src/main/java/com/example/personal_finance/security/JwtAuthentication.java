package com.example.personal_finance.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final UserDetails userDetails;

    public JwtAuthentication(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // No credentials needed for JWT
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
