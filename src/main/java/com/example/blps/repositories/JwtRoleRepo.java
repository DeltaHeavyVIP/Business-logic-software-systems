package com.example.blps.repositories;

import com.example.blps.model.JwtRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRoleRepo extends JpaRepository<JwtRole, Integer> {
}
