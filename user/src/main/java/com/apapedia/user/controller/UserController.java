package com.apapedia.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URI;

import com.apapedia.user.config.JwtService;
import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.RegisterSellerRequestDTO;
import com.apapedia.user.model.SellerCategory;
import com.apapedia.user.model.User;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.net.http.HttpResponse;

import java.net.http.HttpRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.http.HttpClient;

@Controller
public class UserController {

    @Autowired
    JwtService jwtService;

    // FRONTEND BLM SELESAI

    // @GetMapping("/")
    // public String loginForm(Model model) {

    //     var authDTO = new AuthenticationRequest();
    //     model.addAttribute("authDTO", authDTO);

    //     return "login.html";
    // }

    // @PostMapping("/")
    // public String submitFormLogin(@Valid @ModelAttribute AuthenticationRequest authDTO,
    //         HttpServletResponse httpResponse, Model model, RedirectAttributes redirectAttributes)
    //         throws IOException, InterruptedException {

    //     JsonObject jsonBody = new JsonObject();
    //     jsonBody.addProperty("email", authDTO.getEmail());
    //     jsonBody.addProperty("password", authDTO.getPassword());

    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create("http://localhost:8080/api/authentication/authenticate"))
    //             .header("content-type", "application/json")
    //             .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
    //             .build();

    //     HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    //     String responseBody = response.body();

    //     var token = jwtService.getToken();

    //     if (token == null) {
    //         // Jika token kosong, tambahkan pesan kesalahan ke redirectAttributes
    //         redirectAttributes.addFlashAttribute("error", "Wrong email or password");
    //         return "redirect:/"; // Ganti dengan path halaman login kamu
    //     } else {
    //         Cookie cookie = new Cookie("jwtToken", token);
    //         cookie.setPath("/");
    //         cookie.setSecure(true);
    //         cookie.setMaxAge(24 * 60 * 60); // 24 hours in seconds

    //         // Add the cookie to the response
    //         httpResponse.addCookie(cookie);
    //         System.out.println("Login Success");



            
    //         return "redirect:/dashboard/seller";
    //     }

    // }

    @GetMapping("/")
    public String registerForm(Model model) {
        var sellerDTO = new RegisterSellerRequestDTO();
        model.addAttribute("sellerDTO", sellerDTO);

        var listCategory = SellerCategory.values();

        model.addAttribute("listCategory", listCategory);

        return "register.html";
    }

    @PostMapping("/register/seller")
    public String submitFormRegister(@Valid @ModelAttribute RegisterSellerRequestDTO sellerDTO)
            throws IOException, InterruptedException {

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("name", sellerDTO.getName());
        jsonBody.addProperty("username", sellerDTO.getUsername());
        jsonBody.addProperty("password", sellerDTO.getPassword());
        jsonBody.addProperty("email", sellerDTO.getEmail());
        jsonBody.addProperty("address", sellerDTO.getAddress());
        jsonBody.addProperty("category", sellerDTO.getCategory().toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/api/authentication/register/seller"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        return "success-register.html";

    }

    @GetMapping("/dashboard/seller")
    public String dashboardSeller(HttpServletRequest httpServletRequest) throws IOException, InterruptedException {

        System.out.println("CP0");
        // Retrieve cookies from the request
        Cookie[] cookies = httpServletRequest.getCookies();

        System.out.println(cookies);
        // Search for the "jwtToken" cookie

        if (cookies == null) {
            return "access-denied.html";
        }

        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if ("jwtToken".equals(cookie.getName())) {
                var token = jwtService.getToken();
                if (token == null) {
                    System.out.println("CP1");
                    return "access-denied.html";
                }
            }            
        }
        System.out.println("CP3");
        return "dashboard-authenticated.html";
    }

}
