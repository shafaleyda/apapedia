package com.apapedia.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.user.config.JwtService;
import com.apapedia.user.dto.request.LoginJwtRequestDTO;
import com.apapedia.user.model.User;
import com.apapedia.user.repository.UserDb;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDb userDb;

    @Autowired
    private JwtService jwtService;

    @Override
    public String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO) {
    
        String username = loginJwtRequestDTO.getUsername();
        String name = loginJwtRequestDTO.getName();

        User user = userDb.findByUsername(username);

        if (user != null) {

            user.setName(name);
            user.setPassword("bacabaca");
            user.setUsername(username);
            userDb.save(user);
        } else {

            throw new IllegalArgumentException("User not found");
        }

        return jwtService.generateToken(user);
    }

}
