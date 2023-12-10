package com.apapedia.frontend.controller;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class OrderController {
    @GetMapping("/orderHistory")
    public String viewUserOrderHistory(Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/api/user/user-loggedin";
            ResponseEntity<Map<String, Object>> sellerResponse = restTemplate.exchange(
                url, 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

            if (sellerResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userLoggedIn = sellerResponse.getBody();
                String sellerId = (String) userLoggedIn.get("id");

                final String uri = "http://localhost:8080/order/seller/" + sellerId; 

                ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

                Map<String, Object> myResponse = responseEntity.getBody();
                List<Map<String, Object>> ordersRaw = (List<Map<String, Object>>) myResponse.get("data");

                List<Map<String, Object>> orderList = new ArrayList<>();
                if (!ordersRaw.isEmpty()) {
                    for (Map<String, Object> orderDetails : ordersRaw) {
                        Map<String, Object> orderMap = (Map<String, Object>) orderDetails.get("order");
                        List<Map<String, Object>> listOrderItem = (List<Map<String, Object>>) orderDetails.get("listOrderItem");

                        Map<String, Object> orderDetailsMap = new HashMap<>();
                        orderDetailsMap.put("order", orderMap);
                        orderDetailsMap.put("listOrderItem", listOrderItem);

                        orderList.add(orderDetailsMap);
                    }
                }

                model.addAttribute("orderList", orderList);
                System.out.println(orderList);
                return "view-user-order";

            } else{
                return "error";
            }

        } catch (Exception e){
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error"; 
        }
    }

    @GetMapping("/withdraw")
    public String getWithdrawUser(Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/api/user/user-loggedin";
            ResponseEntity<Map<String, Object>> sellerResponse = restTemplate.exchange(
                url, 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

            if (sellerResponse.getStatusCode().is2xxSuccessful()) {
                Object seller = sellerResponse.getBody();
                model.addAttribute("seller", seller);
                return "withdraw-page";
            } else {
                return "error";
            }
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error"; 
        }
    
    }

    @PostMapping("/withdraw") //update balance here
    public String withdrawAllBalance(@RequestParam("amount") int amount, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/api/user/user-loggedin";
            ResponseEntity<Map<String, Object>> sellerResponse = restTemplate.exchange(
                url, 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

            if (sellerResponse.getStatusCode().is2xxSuccessful()) {
                // This is the seller object
                Object seller = sellerResponse.getBody();
                Map<String, Object> userLoggedIn = sellerResponse.getBody();
                String sellerId = (String) userLoggedIn.get("id");
                model.addAttribute("seller", seller);

                if (amount == 0){
                    return "withdraw-page";
                }

                amount = -amount;
                final String uriUpdateBalance = "http://localhost:8081/api/user/" + sellerId + "/balance?amount=" + amount; 

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                ResponseEntity<String> balanceResponse = restTemplate.exchange(
                    uriUpdateBalance,
                    HttpMethod.PUT,
                    null,  
                    String.class
                );

                if (balanceResponse.getStatusCode().is2xxSuccessful()) {
                    String responseBody = balanceResponse.getBody();
                    model.addAttribute("message", responseBody);
                    return "successful";
                } else {
                    return "error";
                }

            } else {
                return "error";
            }
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error"; 
        }
    
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

            return "successful"; 
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error"; 
        }
    }
}
