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

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class OrderController {
    @GetMapping("/orderHistory")
    public String viewUserOrderHistory(Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
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
                        return "view-user-order";

                    } else{
                        return "error";
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    return "error";
                }
            }
        }

        return "user/access-denied.html";

    }

    @GetMapping("/withdraw")
    public String getWithdrawUser(Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException{
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
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
                    e.printStackTrace();
                    return "error";
                }
            }
        }

        return "user/access-denied.html";
    }

    @PostMapping("/withdraw") //update balance here
    public String withdrawAllBalance(@RequestParam("amount") int amount, Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException{
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
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
                        Map<String, Object> userLoggedIn = sellerResponse.getBody();
                        String sellerId = (String) userLoggedIn.get("id");
                        int sellerBalance = (int) userLoggedIn.get("balance");
                        model.addAttribute("seller", seller);

                        if (amount == 0){
                            return "withdraw-denied";
                        } else if (amount > sellerBalance){
                            return "withdraw-denied";
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
                    e.printStackTrace();
                    return "error";
                }
            }
        }

        return "user/access-denied.html";
    }

    @PostMapping("/updateStatus/{orderId}")
    public String updateStatus(@PathVariable("orderId") String id, @RequestParam("status") int status, Model model, HttpServletRequest httpServletRequest)
            throws IOException, InterruptedException{

        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
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
                    e.printStackTrace();
                    return "error";
                }
            }
        }

        return "user/access-denied.html";
    }
}
