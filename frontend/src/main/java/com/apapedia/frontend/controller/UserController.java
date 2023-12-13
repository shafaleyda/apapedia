package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

import com.apapedia.frontend.dto.RegisterSellerRequestDTO;
import com.apapedia.frontend.model.SellerCategory;
import com.google.gson.JsonObject;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.*;
import java.time.LocalDate;

@Controller
public class UserController {
    String baseUrlCatalogue = "http://localhost:8082";
    String baseUrlOrder = "http://localhost:8080";
    String baseUrlUser = "http://localhost:8081";

    @GetMapping("/")
    public String registerForm(Model model) throws IOException, InterruptedException {

        var sellerDTO = new RegisterSellerRequestDTO();
        model.addAttribute("sellerDTO", sellerDTO);

        var listCategory = SellerCategory.values();

        model.addAttribute("listCategory", listCategory);

        return "user/register.html";
    }

    @PostMapping("/register/seller")
    public String submitFormRegister(@Valid @ModelAttribute RegisterSellerRequestDTO sellerDTO,
                                     RedirectAttributes redirectAttributes)
            throws IOException, InterruptedException {

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("name", sellerDTO.getName());
        jsonBody.addProperty("username", sellerDTO.getUsername());
        jsonBody.addProperty("password", sellerDTO.getPassword());
        jsonBody.addProperty("email", sellerDTO.getEmail());
        jsonBody.addProperty("address", sellerDTO.getAddress());
        jsonBody.addProperty("category", sellerDTO.getCategory().toString());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/authentication/register/seller"))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if (response.statusCode() != 200) {
                redirectAttributes.addFlashAttribute("errorMessage", "Gagal melakukan registrasi penjual");
                return "redirect:/failed-register";
            }

        } catch (IOException | InterruptedException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Terjadi kesalahan saat melakukan registrasi: " + e.getMessage());
            return "redirect:/failed-register";
        }

        return "user/success-register.html";

    }

    @GetMapping("/dashboard/seller")
    public String dashboardSeller(Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
                RestTemplate restTemplate = new RestTemplate();
                String urlLogin = baseUrlUser + "/api/user/user-loggedin";

                ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class);
                //System.out.println(userLoggedIn);

                if(userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
                    ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                            urlLogin,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });
                    Map<String, Object> responseBody = userResponse.getBody();
                    UUID seller = UUID.fromString(responseBody.get("id").toString());

                    String url = baseUrlCatalogue + "/api/catalog/seller/" + seller.toString();

                    //Catalog
                    List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                    model.addAttribute("catalogData", catalogData);
                    model.addAttribute("valid", Boolean.TRUE);

                    //Chart
                    String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                    ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                            urlChart,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<LocalDate, Integer>>() {}
                    );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        Map<LocalDate, Integer> mapTotalOrdersPerDay = response.getBody();
                        Map<String, Integer> salesChartStringKeys = new HashMap<>();
                        for (Map.Entry<LocalDate, Integer> entry : mapTotalOrdersPerDay.entrySet()) {
                            String dateStringKey = entry.getKey().toString();
                            salesChartStringKeys.put(dateStringKey, entry.getValue());
                        }
                        model.addAttribute("listCatalogChart", mapTotalOrdersPerDay);
                    }
                }
                return "catalog/seller-viewall-catalog";
            }
        }
        return "user/access-denied.html";
    }

    @GetMapping("/dashboard/seller/guest")
    public String dashboardSellerGuest(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlCatalogue + "/api/catalog/all";

        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

        model.addAttribute("catalogData", catalogData);
        model.addAttribute("valid", Boolean.TRUE);
        return "catalog/guest-viewall-catalog";
    }

    @GetMapping("/failed-register")
    public String failedRegister() {

        return "user/failed-register.html";
    }

    @GetMapping("/failed-login")
    public String failedLogin() throws Exception {
        return "user/failed-login.html";
    }

    @GetMapping("/access-denied")
    public String failedEnter() throws Exception {
        return "user/access-denied.html";
    }

}
