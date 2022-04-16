package com.example.blps.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDto {
    private String cardNumber;
    private LocalDate cardDateEnd;
    private Integer cardCVC;
    private Integer money;
}
