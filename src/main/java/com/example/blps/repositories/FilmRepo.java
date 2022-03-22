package com.example.blps.repositories;

import com.example.blps.model.Films;
import com.example.blps.model.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepo extends JpaRepository<Films, Integer> {
    Optional<Films> findById(Integer id);

    List<Films> findFilmEntitiesByGenreFilm(Genres genre);
}
