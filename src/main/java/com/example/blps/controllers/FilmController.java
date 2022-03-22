package com.example.blps.controllers;

import com.example.blps.dto.IdFilmDto;
import com.example.blps.model.Films;
import com.example.blps.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("film")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping("all")
    public List<Films> findAllFilm() {
        return filmService.getAllFilm();
    }

    @PostMapping("select")
    public Optional<Films> findSelectFilm(@Valid @RequestBody IdFilmDto data) {
        return filmService.getSelectFilm(data.getFilmId());
    }

//    @PostMapping("/payment")
//    public ResponseEntity<?> payFilm(@Valid @RequestBody CardDto data){
//        return ResponseEntity.ok(payUrl(CardDto.getMoney()));
//    }

}
