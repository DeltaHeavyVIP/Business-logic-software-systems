package com.example.blps.service;

import com.example.blps.dto.CardDTO;
import com.example.blps.dto.PaymentDTO;
import com.example.blps.exception.ResourceNotFoundException;
import com.example.blps.model.Cards;
import com.example.blps.model.Films;
import com.example.blps.model.Users;
import com.example.blps.repositories.CardsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    @Autowired
    private CardsRepo cardsRepo;

    public List<Cards> modifyCardMoneyIfExist(PaymentDTO paymentDTO) {
        List<Cards> cards = cardsRepo.findCardsByUser_Id(paymentDTO.getUserId());

        if(cards.size() == 0){
            throw new ResourceNotFoundException("error no card found");
        } else {
            boolean flag = false;
            for (Cards i : cards){
                if (i.getMoney() > paymentDTO.getCost()) {
                    i.setMoney(i.getMoney()-paymentDTO.getCost());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new ResourceNotFoundException("error no money found");
            }

        }
        return cards;
    }

    public Cards addCard(CardDTO cardDTO) {

        String cardNumber = cardDTO.getCardNumber();
        if (cardsRepo.countCardsByCardNumber(cardNumber) != 0) {
            throw new ResourceNotFoundException("this card already exist");
        }

        Cards newCard = new Cards();
        newCard.setCardNumber(cardDTO.getCardNumber());
        newCard.setCardCVC(cardDTO.getCardCVC());
        newCard.setMoney(cardDTO.getMoney());
        newCard.setCardDateEnd(cardDTO.getCardDateEnd());
        newCard = cardsRepo.save(newCard);

        return newCard;
    }

    public boolean checkCardInformation(CardDTO cardDTO){
        String regex_cardNumber = "(^$|[0-9]{16})";
        String regex_cardCVC = "(^$|[0-9]{3})";

        if (!cardDTO.getCardNumber().matches(regex_cardNumber)){
            return false;
        }
        if (!cardDTO.getCardCVC().toString().matches(regex_cardCVC)){
            return false;
        }
        if (cardDTO.getMoney() < 0){
            return false;
        }

        if (cardDTO.getCardDateEnd().compareTo(LocalDate.now()) < 0) {
            return false;
        }

        return true;
    }

}
