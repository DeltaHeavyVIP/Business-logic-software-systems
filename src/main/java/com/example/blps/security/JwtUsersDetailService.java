package com.example.blps.security;

import com.example.blps.model.Users;
import com.example.blps.repositories.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUsersDetailService implements UserDetailsService {
    private final UsersRepo usersRepo;

    public JwtUsersDetailService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("User with username " + username + " does not exists");

        return user;
    }

}
