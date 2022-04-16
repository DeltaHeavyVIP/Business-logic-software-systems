package com.example.blps.controllers;

import com.example.blps.dto.FilmDto;
import com.example.blps.exception.ResourceNotFoundException;
import com.example.blps.model.Films;
import com.example.blps.service.FilmsService;
import com.example.blps.service.UsersService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/youtube")
public class FilmController {
    @Autowired
    private FilmsService filmService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 400, message = "Have a problem with films in database"),
    })
    @ApiOperation(value = "get all films", response = Map.class)
    @PostMapping("/allFilms")
    public List<Films> allFilms() {
        return filmService.getAllFilms();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request, film not found"),
    })
    @ApiOperation(value = "get select film", response = Map.class)
    @PostMapping("/selectFilm")
    public Object selectFilm(@RequestBody FilmDto data) {
        try {
            return filmService.getSelectFilm(data);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
