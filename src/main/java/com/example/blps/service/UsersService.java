package com.example.blps.service;

import com.example.blps.dto.UserDto;
import com.example.blps.model.Films;
import com.example.blps.model.Users;
import com.example.blps.repositories.CardsRepo;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.GenresRepo;
import com.example.blps.repositories.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

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

    public Users addUser(UserDto data) {
        Users users = usersRepo.findUsersByPhoneNumber(data.getPhoneNumber());
        if (users == null) {
            Users newUsers = new Users();
            newUsers.setPhoneNumber(data.getPhoneNumber());
            newUsers = usersRepo.save(newUsers);
            return newUsers;
        }
        return users;
    }

    public void addFilmToUser(Integer id, Integer filmId) {

        Users user = usersRepo.findUsersById(id);
        Films film = filmRepo.findFilmsById(filmId);
        Set<Films> userFilmSet = user.getUserFilm();
        boolean flag = true;
        for (Films i : userFilmSet) {
            if (Objects.equals(i.getId(), film.getId())) {
                flag = false;
            }
        }
        if (flag) {
            userFilmSet.add(film);
        }
        user.setUserFilm(userFilmSet);
        user = usersRepo.save(user);
    }
}
