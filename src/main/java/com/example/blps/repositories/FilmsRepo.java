package com.example.blps.repositories;

import com.example.blps.model.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmsRepo extends JpaRepository<Films, Integer> {
    Films findFilmsById(Integer id);

}
