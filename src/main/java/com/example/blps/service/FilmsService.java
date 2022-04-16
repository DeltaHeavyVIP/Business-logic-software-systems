package com.example.blps.service;

import com.example.blps.dto.FilmDto;
import com.example.blps.exception.ResourceNotFoundException;
import com.example.blps.model.Films;
import com.example.blps.repositories.CardsRepo;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.GenresRepo;
import com.example.blps.repositories.UsersRepo;
import com.example.blps.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmsService {

    @Autowired
    private CardsRepo cardsRepo;

    @Autowired
    private FilmsRepo filmRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CardService cardService;

    @Autowired
    private UsersService usersService;


    public List<Films> getAllFilms() {
        return filmRepo.findAll();

    }

    public String getSelectFilm(FilmDto data) {
        Films film = filmRepo.findFilmsById(data.getFilmId());
        if (film == null) {
            throw new ResourceNotFoundException("Данный фильм не найден в базе данных");
        }
        if (film.getCost() != 0) {
            cardService.modifyCardMoneyIfExist(usersRepo.findByUsername(jwtProvider.getUsernameFromToken(data.getToken())).getId(), film);
        }
        usersService.addFilmToUser(usersRepo.findByUsername(jwtProvider.getUsernameFromToken(data.getToken())).getId(), film.getId());
        return film.getToken();
    }
}
