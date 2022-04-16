package com.example.blps.controllers;

import com.example.blps.dto.LoginDto;
import com.example.blps.dto.RegisterDto;
import com.example.blps.model.Users;
import com.example.blps.security.JwtProvider;
import com.example.blps.service.UsersService;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity register(@RequestBody RegisterDto registerDto) {
        try {
            if (usersService.findUserByUserName(registerDto.getUsername()) != null) {
                throw new NonUniqueResultException("User with this username has been already registered");
            }
            Users user = usersService.register(registerDto);
            String token = jwtProvider.createToken(registerDto.getUsername());
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", registerDto.getUsername());
            response.put("refreshToken", user.getRefreshToken());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "User with username " + registerDto.getUsername() + " has already been registered");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        try {
            Users user = usersService.findUserByUserName(loginDto.getUserName());
            if (user == null)
                throw new UsernameNotFoundException("User with username " + loginDto.getUserName() + " not found");
            String token = jwtProvider.createToken(loginDto.getUserName());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginDto.getPassword()));

            Map<Object, Object> response = new HashMap<>();
            response.put("username", loginDto.getUserName());
            response.put("refreshToken", user.getRefreshToken());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "User with username " + loginDto.getUserName() + " not found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (AuthenticationException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "Wrong password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}
