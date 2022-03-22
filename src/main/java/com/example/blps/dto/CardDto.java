package com.example.blps.dto;

import lombok.Data;

@Data
public class CardDto {
    private Integer filmId;
    private Integer cardNumber;
    private String cardDateEnd;
    private Integer cardCVC;
}
