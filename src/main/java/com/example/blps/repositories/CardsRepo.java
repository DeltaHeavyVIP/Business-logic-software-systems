package com.example.blps.repositories;

import com.example.blps.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepo extends JpaRepository<Cards, Integer> {
    List<Cards> findCardsByUser_Id(Integer id);
    int countCardsByCardNumber(String cardNumber);
}
