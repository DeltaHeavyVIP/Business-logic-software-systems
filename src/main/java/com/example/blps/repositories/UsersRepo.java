package com.example.blps.repositories;

import com.example.blps.model.Films;
import com.example.blps.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users findUsersByPhoneNumber(String phoneNumber);

}
