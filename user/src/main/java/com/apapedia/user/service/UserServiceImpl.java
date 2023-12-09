package com.apapedia.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        System.out.println("LOGINJWTADMIN");
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }

        String username = loginJwtRequestDTO.getUsername();
        String name = loginJwtRequestDTO.getName();

        // var tokenRegis = jwtService.getToken();
        System.out.println("CP 1");
        
        // UUID idUser = jwtService.extractUserId(tokenRegis);
        
        // User user = userDb.findById(idUser).get();

        
        
        User user = userDb.findByUsername(username);

        System.out.println("CP 2");

        System.out.println(user);

        if (user != null) {
            System.out.println("CP 3");
            user.setName(name);
            user.setPassword("bacabaca");
            user.setUsername(username);
            userDb.save(user);
        } else{
            System.out.println("CP 4");
            throw new IllegalArgumentException("User not found"); 
        }

        System.out.println("CP 5");
        return jwtService.generateToken(user);
    }


}
