package com.example.blps.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

}
