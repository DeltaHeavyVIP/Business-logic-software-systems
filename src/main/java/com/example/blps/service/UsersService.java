package com.example.blps.service;

import com.example.blps.model.Users;
import com.example.blps.repositories.CardsRepo;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.GenresRepo;
import com.example.blps.repositories.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private CardsRepo cardsRepo;

    @Autowired
    private FilmsRepo filmRepo;

    @Autowired
    private GenresRepo genresRepo;

    @Autowired
    private UsersRepo usersRepo;

    public Users addUser(String phoneNumber) {
        Users users = usersRepo.findUsersByPhoneNumber(phoneNumber);
        if(users == null){
            Users newUsers = new Users();
            newUsers.setPhoneNumber(phoneNumber);
            return newUsers;
        }
        return users;
    }
}
