package com.example.blps.service;

import com.example.blps.dto.RegisterDto;
import com.example.blps.model.Films;
import com.example.blps.model.JwtRole;
import com.example.blps.model.Users;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.JwtRoleRepo;
import com.example.blps.repositories.UsersRepo;
import com.example.blps.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionStatus;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FilmsRepo filmRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtRoleRepo jwtRoleRepo;


    @Autowired
    private TransactionTemplate transactionTemplate;

    public Users findUserByUserName(String userName) {
        return usersRepo.findByUsername(userName);
    }

    public Users register(RegisterDto data) {
        Users newUser = new Users();
        newUser.setFirstName(data.getFirstName());
        newUser.setLastName(data.getLastName());
        newUser.setRefreshToken(Base64.getEncoder().encodeToString((UUID.randomUUID() + "&" + data.getUsername()).getBytes()));
        newUser.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
        newUser.setUsername(data.getUsername());
        Set<JwtRole> userRole = new HashSet<>();
        String[] rolesFromDto = data.getRole().split(",");
        List<JwtRole> roleFromDatabase = jwtRoleRepo.findAll();
        for (String roleDto : rolesFromDto) {
            for (JwtRole roleDatabase : roleFromDatabase) {
                if (roleDto.equals(roleDatabase.getAuthority())) {
                    userRole.add(roleDatabase);
                }
            }
        }
        newUser.setRoles(userRole);
        Users user = usersRepo.save(newUser);
        return user;
    }

    public void addFilmToUser(Integer userId, Integer filmId) {
        Users user = usersRepo.findUsersById(userId);
        Films film = filmRepo.findFilmsById(filmId);
        Set<Films> userFilmSet = user.getUserFilm();

        transactionTemplate.execute(
                status -> {
                    boolean flag = true;
                    for (Films i : userFilmSet) {
                        if (Objects.equals(i.getId(), film.getId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        userFilmSet.add(film);
                    }
                    user.setUserFilm(userFilmSet);
                    usersRepo.save(user);
                    return user;
                }
        );

    }
}
