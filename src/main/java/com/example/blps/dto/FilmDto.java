package com.example.blps.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FilmDto {
    private Integer filmId;
    private Integer userId;

    public FilmDto(Integer filmId, Integer userId) {
    }
}
