package com.apapedia.frontend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {
    @GetMapping("/orderHistory")
    public String viewUserOrderHistory(Model model) {
        RestTemplate restTemplate = new RestTemplate();

        // UUID id = loggedInSeller.getId();
        final String uri = "http://localhost:8080/order/seller/dc4cf886-ab46-49a4-9fbb-458cfdd43f69"; // masih hardcode, belum ambil id

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {
            });

        Map<String, Object> myResponse = responseEntity.getBody();
        List<Object> ordersRaw = (List<Object>) myResponse.get("data");

        if (!ordersRaw.isEmpty()) {
            model.addAttribute("listOrder", ordersRaw);
            return "view-user-order";
        }

        return "error"; // Render error HTML
    }

    @GetMapping("/withdraw")
    public String viewCompletedOrders(Model model) { // consume api user #6 (belum ada)
        RestTemplate restTemplate = new RestTemplate();
        
        // UUID id = loggedInSeller.getId();
        final String uri = "http://localhost:8080/order/seller/dc4cf886-ab46-49a4-9fbb-458cfdd43f69"; // masih hardcode, belum ambil id

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {
            });

        Map<String, Object> myResponse = responseEntity.getBody();
        List<Object> ordersRaw = (List<Object>) myResponse.get("data");

        if (!ordersRaw.isEmpty()) {
            model.addAttribute("listOrder", ordersRaw);
            return "withdraw-page";
        }

        return "error"; // Render error HTML
    }

    @PostMapping("/withdraw/{orderId}") //update balance here
    public String withdrawOrderFunds(@PathVariable("orderId") String id, Model model) {
        // api user #6 not available yet
        // final String uri = "http://localhost:8080/user/updateBalance/" + id; 
        return "successful";
    }


    @PostMapping("/updateStatus/{orderId}")
    public String updateStatus(@PathVariable("orderId") String id, @RequestParam("status") int status, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
    
            final String uri = "http://localhost:8080/order/update/" + id;
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("status", status);
    
            HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestBody, headers);
    
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                requestEntity,
                Void.class
            );
            Object updatedOrder = responseEntity.getBody();
    
            if (updatedOrder != null) {
                return "successful";
            }
    
            return "error"; 
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error"; 
        }
    }

    
}
