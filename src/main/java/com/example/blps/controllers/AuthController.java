package com.example.blps.controllers;

import com.example.blps.dto.LoginDto;
import com.example.blps.dto.RegisterDto;
import com.example.blps.model.JwtUsers;
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
            if (usersService.findByPhoneNumber(registerDto.getPhoneNumber()) != null) {
                throw new NonUniqueResultException("User with this phone number has been already registered");
            }
            JwtUsers jwtUser = usersService.register(registerDto);
            String token = jwtProvider.createToken(registerDto.getPhoneNumber());
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", registerDto.getUsername());
            response.put("refreshToken", jwtUser.getRefreshToken());
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
            Users user = usersService.findByPhoneNumber(loginDto.getPhoneNumber());
            if (user == null)
                throw new UsernameNotFoundException("User with phone number " + loginDto.getPhoneNumber() + " not found");
            String token = jwtProvider.createToken(loginDto.getPhoneNumber());
            JwtUsers jwtUsers = user.getJwtUser();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtUsers.getUsername(), loginDto.getPassword()));

            Map<Object, Object> response = new HashMap<>();
            response.put("username", jwtUsers.getUsername());
            response.put("refreshToken", jwtUsers.getRefreshToken());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "User with phone number " + loginDto.getPhoneNumber() + " not found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (AuthenticationException ex) {
            Map<Object, Object> response = new HashMap<>();
            response.put("description", "Wrong password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}
