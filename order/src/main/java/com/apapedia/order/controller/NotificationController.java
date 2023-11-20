package com.apapedia.order.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.apapedia.order.service.OrderService;

import jakarta.validation.Valid;

@Controller
public class NotificationController {
    @Autowired
    OrderService orderService;

    @PutMapping("order/releaseFunds")
    public String releaseFundsToSeller(@Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO){

        RestTemplate restTemplate = new RestTemplate();

        // Call the update balance endpoint in the User service
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                "http://user-service-host/user/iniuriyangupdatebalance",
                HttpMethod.PUT,
                new HttpEntity<>(updateUserRequestDTO),
                User.class);

        // Check the response and render HTML accordingly
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return "success-release-funds.html"; // Render HTML as needed
        } else {
            return "error.html"; // Render error HTML
        }
    }

}
