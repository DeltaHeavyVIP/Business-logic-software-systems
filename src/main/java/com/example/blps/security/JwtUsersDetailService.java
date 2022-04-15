package com.example.blps.security;

import com.example.blps.model.JwtUsers;
import com.example.blps.repositories.JwtUsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUsersDetailService implements UserDetailsService {
    private final JwtUsersRepo jwtUsersRepo;

    public JwtUsersDetailService(JwtUsersRepo jwtUsersRepo) {
        this.jwtUsersRepo = jwtUsersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUsers jwtUser = jwtUsersRepo.findByUsername(username);

        if (jwtUser == null) throw new UsernameNotFoundException("User with username " + username + " does not exists");

        return jwtUser;
    }

}
