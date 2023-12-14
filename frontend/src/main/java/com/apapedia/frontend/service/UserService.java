package com.apapedia.frontend.service;

import com.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.response.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
//    private final RestTemplate restTemplate;
    private static final String baseUrlUser = "http://localhost:8081/api/user/";

    public ResponseEntity<UserDTO> getUserById(String id) {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                baseUrlUser + id,
                HttpMethod.GET,
                null,
                UserDTO.class
        );
    }

    public ResponseEntity<UserDTO> updateUserById(String id, UpdateUserRequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(baseUrlUser + "update/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(requestDTO),
                UserDTO.class
        );
    }

    public ResponseEntity<String> deleteUserById(String id) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(baseUrlUser + "delete/" + id,
                HttpMethod.DELETE,
                null,
                String.class
        );
    }


    public void deleteJwtTokenCookie(HttpServletResponse response) {
        Cookie jwtTokenCookie = new Cookie("jwtToken", null);
        jwtTokenCookie.setMaxAge(0);
        jwtTokenCookie.setPath("/");
        response.addCookie(jwtTokenCookie);
    }

}
