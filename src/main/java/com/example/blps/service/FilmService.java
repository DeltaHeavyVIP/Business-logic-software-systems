package com.example.blps.service;

import com.example.blps.model.Films;
import com.example.blps.repositories.FilmRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    private FilmRepo filmRepo;

    public List<Films> getAllFilm() {
        return filmRepo.findAll();
    }

    public Optional<Films> getSelectFilm(Integer filmId) {
        return filmRepo.findById(filmId);
    }

    public Optional<Films> getFilm(Integer filmId) {
        return filmRepo.findById(filmId);
    }

}
