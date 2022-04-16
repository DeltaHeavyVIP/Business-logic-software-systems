package com.example.blps.controllers;

import com.example.blps.dto.RefreshDto;
import com.example.blps.model.Users;
import com.example.blps.security.JwtProvider;
import com.example.blps.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/refresh/")
public class RefreshController {
    private final JwtProvider jwtProvider;
    private final UsersService usersService;

    public RefreshController(JwtProvider jwtProvider, UsersService usersService){
        this.jwtProvider=jwtProvider;
        this.usersService=usersService;
    }

    @PostMapping("/token")
    public ResponseEntity refreshToken(@RequestBody RefreshDto refreshDto){
        String phoneNumber = new String(Base64.getDecoder().decode(refreshDto.getRefreshToken())).split("&")[1];
        Users user = usersService.findByPhoneNumber(phoneNumber);
        Map<Object, Object> response = new HashMap<>();

        if(user == null) throw new UsernameNotFoundException("User with phone number " + phoneNumber + " not found");

        if(user.getJwtUser().getRefreshToken().equals(refreshDto.getRefreshToken())) {
            String token = jwtProvider.createToken(phoneNumber);
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("phoneNumber", phoneNumber);
            response.put("refreshToken", user.getJwtUser().getRefreshToken());
            response.put("token", token);
        }else throw new BadCredentialsException("Invalid refresh token");

        return ResponseEntity.ok(response);
    }
}