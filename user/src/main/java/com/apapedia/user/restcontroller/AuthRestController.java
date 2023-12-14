package com.apapedia.user.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.user.dto.request.LoginJwtRequestDTO;
import com.apapedia.user.dto.response.LoginJwtResponseDTO;
import com.apapedia.user.service.UserService;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    UserService userService;

    @PostMapping("/auth/login-jwt-webadmin")
    public ResponseEntity<?> loginJwtAdmin(@RequestBody LoginJwtRequestDTO loginJwtRequestDTO) {
        
        try {
            String jwtToken = userService.loginJwtAdmin(loginJwtRequestDTO);
            return new ResponseEntity<>(new LoginJwtResponseDTO(jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception in auth");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
