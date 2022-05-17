package com.example.blps.service;

import com.example.blps.dto.FilmDto;
import com.example.blps.exception.ResourceNotFoundException;
import com.example.blps.message.producer.MessageService;
import com.example.blps.model.Films;
import com.example.blps.model.Genres;
import com.example.blps.repositories.CardsRepo;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.GenresRepo;
import com.example.blps.repositories.UsersRepo;
import com.example.blps.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.jms.Message;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmsService {

    @Autowired
    private GenresRepo genresRepo;

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

    @Autowired
    private TransactionTemplate transactionTemplate;

    public List<Films> getAllFilms() {
        return filmRepo.findAll();
    }

    public String getSelectFilm(FilmDto data) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Films film = filmRepo.findFilmsById(data.getFilmId());
        if (film == null) {
            throw new ResourceNotFoundException("Данный фильм не найден в базе данных");
        }

        transactionTemplate.execute(
                status -> {
                    if (film.getCost() != 0) {
                        cardService.modifyCardMoneyIfExist(usersRepo.findByUsername(userName).getId(), film);
                    }

                    usersService.addFilmToUser(usersRepo.findByUsername(userName).getId(), film.getId());
                    return film;
                }
                );

        return film.getToken();
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void informationAboutDB() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        log.info("Amount of users :" + usersRepo.count());
        log.info("Amount of films :" + filmRepo.count());
        log.info("Amount of genres :" + genresRepo.count());
        log.info("End collecting information! Time: " + formatter.format(new Date()));
    }
}
