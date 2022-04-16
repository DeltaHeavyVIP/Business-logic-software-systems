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
        return usersRepo.findUsersByPhoneNumber(phoneNumber);
    }

    public JwtUsers register(RegisterDto data) {
        Users newUser = new Users();
        newUser.setPhoneNumber(data.getPhoneNumber());
        newUser.setFirstName(data.getFirstName());
        newUser.setLastName(data.getLastName());
        newUser = usersRepo.save(newUser);

        JwtUsers newJwtUsers = new JwtUsers();
        newJwtUsers.setRefreshToken(Base64.getEncoder().encodeToString((UUID.randomUUID() + "&" + data.getUsername()).getBytes()));
        newJwtUsers.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
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
        newJwtUsers.setUser(newUser);
        JwtUsers jwtUser = jwtUsersRepo.save(newJwtUsers);
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
                break;
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
