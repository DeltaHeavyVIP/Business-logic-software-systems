package com.example.blps.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CardDto {
    private String cardNumber;
    private LocalDate cardDateEnd;
    private Integer cardCVC;
    private Integer money;
}
