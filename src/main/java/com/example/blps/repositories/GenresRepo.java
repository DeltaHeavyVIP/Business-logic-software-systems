package com.example.blps.repositories;

import com.example.blps.model.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepo extends JpaRepository<Genres, Integer> {
}
