package com.example.blps.controllers;


import com.example.blps.dto.CardDto;
import com.example.blps.dto.PaymentDto;
import com.example.blps.model.Cards;
import com.example.blps.service.CardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request, wrong data "),
    })
    @ApiOperation(value = "add card", response = Map.class)
    @PostMapping("/addCard")
    public Cards addCard(@RequestBody CardDto data) {
        return cardService.addCard(data);
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request, wrong data "),
    })
    @ApiOperation(value = "modify card money", response = Map.class)
    @PostMapping("/modifyCardMoney")
    public List<Cards> addCard(@RequestBody PaymentDto data) {
        return cardService.modifyCardMoneyIfExist(data);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request, wrong data "),
    })
    @ApiOperation(value = "check card information", response = Map.class)
    @PostMapping("/checkCard")
    public boolean checkCardInformation(@RequestBody CardDto data) {
        return cardService.checkCardInformation(data);
    }



}
