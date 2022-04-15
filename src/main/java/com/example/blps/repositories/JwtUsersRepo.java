package com.example.blps.repositories;

import com.example.blps.model.JwtUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtUsersRepo extends JpaRepository<JwtUsers, Integer> {
    JwtUsers findByUsername(String username);
}
