package com.example.blps.repositories;

import com.example.blps.model.FilmEntity;
import com.example.blps.model.GenreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepo extends CrudRepository<FilmEntity, Integer> {

    Optional<FilmEntity> findById(Integer id);

}
