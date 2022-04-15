package com.example.blps.service;

import com.example.blps.dto.RegisterDto;
import com.example.blps.model.Films;
import com.example.blps.model.JwtRole;
import com.example.blps.model.JwtUsers;
import com.example.blps.model.Users;
import com.example.blps.repositories.FilmsRepo;
import com.example.blps.repositories.JwtRoleRepo;
import com.example.blps.repositories.JwtUsersRepo;
import com.example.blps.repositories.UsersRepo;
import com.example.blps.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private JwtUsersRepo jwtUsersRepo;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtRoleRepo jwtRoleRepo;

    public Users findByPhoneNumber(String phoneNumber) {
        Users user = usersRepo.findUsersByPhoneNumber(phoneNumber);
        return user;
    }

    public JwtUsers register(RegisterDto data) {
        JwtUsers newJwtUsers = new JwtUsers();
        newJwtUsers.setRefreshToken(Base64.getEncoder().encodeToString((UUID.randomUUID() + "&" + data.getUsername()).getBytes()));
        newJwtUsers.setPassword(bCryptPasswordEncoder.encode(newJwtUsers.getPassword()));
        newJwtUsers.setUsername(data.getUsername());
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
        newJwtUsers.setRoles(userRole);
        JwtUsers jwtUser = jwtUsersRepo.save(newJwtUsers);

        Users newUsers = new Users();
        newUsers.setPhoneNumber(data.getPhoneNumber());
        newUsers.setFirstName(data.getFirstName());
        newUsers.setLastName(data.getLastName());
        newUsers.setJwtUser(jwtUser);
        newUsers = usersRepo.save(newUsers);
        return jwtUser;
    }

    public void addFilmToUser(Integer userId, Integer filmId) {
        Users user = usersRepo.findUsersById(userId);
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
        usersRepo.save(user);
    }

    public Integer getUserIdFromToken(String token) {
        JwtUsers jwtUser = jwtUsersRepo.findByUsername(jwtProvider.getUsernameFromToken(token));
        return jwtUser.getUser().getId();
    }
}
