package com.apapedia.frontend.controller;

import com.apapedia.frontend.dto.RegisterSellerRequestDTO;
import com.apapedia.frontend.dto.request.UpdateUserRequestDTO;
//import com.apapedia.frontend.dto.response.UserDTO;

//import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.model.SellerCategory;
import com.apapedia.frontend.setting.Setting;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
// untuk
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    public String redirectError(HttpStatusCode code) {
        if (code.value() == 403) {
            return "403";
        } else if (code.is4xxClientError()) {
            return "error";
        } else {
            return "error";
        }

    }

//    private final UserService userService;


    // ----------- VIEW PROFILE -----------
    @GetMapping("user/profile")
    public String getUserProfile(Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
//        Cookie[] cookies = httpServletRequest.getCookies();
//
//        if (cookies == null) {
//            return "user/access-denied.html";
//        }

//        for (Cookie cookie : cookies) {
//            if (!("jwtToken".equals(cookie.getName()))) {
//                continue;
//            } else {
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
                Object data = sellerResponse.getBody();
                model.addAttribute("data", data);
                return "profile/profile.html";
            } else {
                return "error";
            }
        } catch (Exception e) {
            System.out.println("Caught an exception: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }

    }

    // ----------- UPDATE -----------
    @GetMapping("user/update/{id}")
    public String registerForm(Model model, HttpServletRequest httpServletRequest)
            throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
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
                        model.addAttribute("data", seller);

                        var listCategory = SellerCategory.values();

                        model.addAttribute("listCategory", listCategory);

                        return "profile/update-profile.html";

                    }
                } catch (Exception e) {
                    System.out.println("Caught an exception: " + e.getMessage());
                    e.printStackTrace();
                    return "error";
                }

//        var userDTO = new UpdateUserRequestDTO();
//        model.addAttribute("data", userDTO);
            }

        }

        return null;
    }


    @PostMapping("user/update/{id}")
    public String submitRegisterForm(@Valid @ModelAttribute UpdateUserRequestDTO updateUserDTO,
                                     HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes)
            throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                try {
                } catch (Exception e) {
                    System.out.println("Caught an exception: " + e.getMessage());
                    e.printStackTrace();
                    return "error";
                }
                return "redirect:profile/profile.html";

            }
        }
        return null;
    }

    @DeleteMapping("user/delete")
    public String deleteAccount(Model model,
                                HttpServletRequest httpServletRequest
    ) throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String urlLoggedIn = "http://localhost:8081/api/user/user-loggedin";
                    ResponseEntity<Map<String, Object>> sellerResponse = restTemplate.exchange(
                            urlLoggedIn,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            });

                    if (sellerResponse.getStatusCode().is2xxSuccessful()) {
                        Object data = sellerResponse.getBody();

                        Map<String, Object> userLoggedIn = sellerResponse.getBody();
                        String sellerId = (String) userLoggedIn.get("id");

                        final String urlDelete = "http://localhost:8081/api/user/delete/" + sellerId;

                        ResponseEntity<String> getDeleteResponse = restTemplate.exchange(
                                urlDelete,
                                HttpMethod.DELETE,
                                null,
                                String.class
                        );

                        if (getDeleteResponse.getStatusCode().is2xxSuccessful()) {
                            return "redirect:register.html";
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Caught an exception: " + e.getMessage());
                    e.printStackTrace();
                    return "user/access-denied.html";
                }
            }
        }

        return "user/access-denied.html";
    }


    // ----------- LOGOUT -----------
    @GetMapping("user/logout")
    public ModelAndView logoutSSO(Principal principal, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Set masa berlaku cookie menjadi 0 untuk menghapusnya

        // Menambahkan cookie yang sama dengan nilai null dan masa berlaku yang sudah
        // lewat
        response.addCookie(cookie);

        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }
}