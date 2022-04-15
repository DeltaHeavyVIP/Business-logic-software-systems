package com.example.blps.controllers;

import com.example.blps.dto.UserDto;
import com.example.blps.model.JwtUsers;
import com.example.blps.security.JwtProvider;
import com.example.blps.service.UsersService;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NonUniqueResultException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsersService usersService;

    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDto userDTO) {
        try {
            if (usersService.findByPhoneNumber(userDTO.getPhoneNumber()) != null) {
                throw new NonUniqueResultException("User with such username has been already registered");
            }
            JwtUsers user = usersService.register(userDTO);
            String token = jwtProvider.createToken(userDTO.getUsername());
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", userDTO.getUsername());
            response.put("refreshToken", user.getRefreshToken());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "User with username " + userDTO.getUsername() + " has already been registered");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}
