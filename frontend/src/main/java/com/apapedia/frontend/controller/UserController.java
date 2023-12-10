package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URI;

import com.apapedia.frontend.dto.RegisterSellerRequestDTO;
import com.apapedia.frontend.model.SellerCategory;
import com.google.gson.JsonObject;

import java.net.http.HttpResponse;

import java.net.http.HttpRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.http.HttpClient;

@Controller
public class UserController {

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
    public String dashboardSeller(HttpServletRequest httpServletRequest) throws IOException, InterruptedException {

        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else{
                return "user/dashboard-authenticated.html";
            }
        }

        return "user/access-denied.html";
    }

    @GetMapping("/dashboard/seller/guest")
    public String dashboardSellerGuest() {

        return "user/dashboard-non-authenticated.html";
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