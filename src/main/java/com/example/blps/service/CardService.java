package com.example.blps.service;

import com.example.blps.dto.CardDto;
import com.example.blps.exception.ResourceNotFoundException;
import com.example.blps.model.Cards;
import com.example.blps.model.Films;
import com.example.blps.repositories.CardsRepo;
import com.example.blps.repositories.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    @Autowired
    private CardsRepo cardsRepo;

    @Autowired
    private UsersRepo usersRepo;

    public void modifyCardMoneyIfExist(Integer userId, Films film) {
        List<Cards> cards = cardsRepo.findCardsByUser_Id(userId);

        if (cards.size() == 0) {
            throw new ResourceNotFoundException("error no card found");
        } else {
            boolean flag = false;
            for (Cards i : cards) {
                if (i.getMoney() > film.getCost()) {
                    i.setMoney(i.getMoney() - film.getCost());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new ResourceNotFoundException("error no money found");
            }
        }
    }

    public void addCard(CardDto cardDto) {
        if (!checkCardInformation(cardDto)) {
            throw new ResourceNotFoundException("Bad data in cardDto");
        }
        String cardNumber = cardDto.getCardNumber();
        if (cardsRepo.countCardsByCardNumber(cardNumber) != 0) {
            throw new ResourceNotFoundException("this card already exist");
        }

        Cards newCard = new Cards();
        newCard.setCardNumber(cardDto.getCardNumber());
        newCard.setCardCVC(cardDto.getCardCVC());
        newCard.setMoney(cardDto.getMoney());
        newCard.setCardDateEnd(cardDto.getCardDateEnd());
        newCard.setUser(usersRepo.findUsersById(cardDto.getUserId()));
        cardsRepo.save(newCard);
    }

    public boolean checkCardInformation(CardDto cardDto) {
        String regex_cardNumber = "(^$|[0-9]{16})";
        String regex_cardCVC = "(^$|[0-9]{3})";

        if (!cardDto.getCardNumber().matches(regex_cardNumber)) {
            return false;
        }
        if (!cardDto.getCardCVC().toString().matches(regex_cardCVC)) {
            return false;
        }
        if (cardDto.getMoney() < 0) {
            return false;
        }
        if (cardDto.getCardDateEnd().compareTo(LocalDate.now()) < 0) {
            return false;
        }
        return true;
    }

}
