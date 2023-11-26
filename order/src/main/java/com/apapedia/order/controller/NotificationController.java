package com.apapedia.order.controller;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
    public String releaseFundsToSeller(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        final String uri = "http://user-service-host/user/iniuriyangupdatebalance";

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.PUT,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {

        });

        Map<String, Object> myResponse = responseEntity.getBody();
        Map<String, Object> result = (Map<String, Object>) myResponse.get("result");
        Object priceObject = result.get("price");

        if (priceObject instanceof Number) {
            Long price = ((Number) priceObject).longValue();

            model.addAttribute("orderAmount", price);
            return "success-release-funds.html";
        }
        return "error.html"; // Render error HTML

    }


}
