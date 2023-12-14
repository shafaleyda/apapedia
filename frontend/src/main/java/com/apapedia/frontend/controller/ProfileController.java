package com.apapedia.frontend.controller;

import com.apapedia.frontend.dto.RegisterSellerRequestDTO;
import com.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.response.UserDTO;

//import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.model.SellerCategory;
import com.apapedia.frontend.service.UserService;
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

    private final UserService userService;


    // ----------- VIEW PROFILE -----------
    @GetMapping("user/profile")
    public String getUserProfile(Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
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
//        } catch (Exception e) {
//            System.out.println("Caught an exception: " + e.getMessage());
//            e.printStackTrace();
//            return "error";
                }
            }

//        }
        return "user/access-denied.html";
    }

    // ----------- UPDATE -----------

    @GetMapping("user/update/{id}")
    public String updateForm(@PathVariable String id, Model model) {
        var userDto = userService.getUserById(id);

        if (userDto.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("data", userDto.getBody());
            return "profile/update-profile";
        } else {
            return "error";
        }
    }

    @PostMapping("user/update/{id}")
    public String submitUpdateForm(@ModelAttribute("data")
                         UpdateUserRequestDTO updateUserRequestDTO,
                         @PathVariable("id") String id, Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserById(id, updateUserRequestDTO);
            System.out.println("Sukses updatenih");

            redirectAttributes.addFlashAttribute("message", " Success");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/user/profile";
        } catch (HttpClientErrorException e){
            redirectAttributes.addFlashAttribute("message", " Failed");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "error";
        }

    }

    // ----------- DELETE -----------
    @GetMapping("user/delete/{id}")
    public String delete(@PathVariable String id,
                         Model mode, HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {
        try{
            userService.deleteUserById(id);
            userService.deleteJwtTokenCookie(response);
            System.out.println("Sukses nih");

            redirectAttributes.addFlashAttribute("message", " Success Delete");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/";
        }catch (HttpClientErrorException e){
            redirectAttributes.addFlashAttribute("message", " Failed Delete");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "error";
        }
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

//    @GetMapping("user/update/{id}")
//    public String registerForm(Model model, HttpServletRequest httpServletRequest)
//            throws IOException, InterruptedException {
//        Cookie[] cookies = httpServletRequest.getCookies();
//
//        if (cookies == null) {
//            return "user/access-denied.html";
//        }
//
//        for (Cookie cookie : cookies) {
//            if (!("jwtToken".equals(cookie.getName()))) {
//                continue;
//            } else {
//                try {
//                    RestTemplate restTemplate = new RestTemplate();
//                    String url = "http://localhost:8081/api/user/user-loggedin";
//                    ResponseEntity<Map<String, Object>> sellerResponse = restTemplate.exchange(
//                            url,
//                            HttpMethod.GET,
//                            null,
//                            new ParameterizedTypeReference<Map<String, Object>>() {
//                            });
//
//                    if (sellerResponse.getStatusCode().is2xxSuccessful()) {
//                        Object seller = sellerResponse.getBody();
//                        model.addAttribute("data", seller);
//
//                        var listCategory = SellerCategory.values();
//
//                        model.addAttribute("listCategory", listCategory);
//
//                        return "profile/update-profile.html";
//
//                    }
//                } catch (Exception e) {
//                    System.out.println("Caught an exception: " + e.getMessage());
//                    e.printStackTrace();
//                    return "error";
//                }
//
////        var userDTO = new UpdateUserRequestDTO();
////        model.addAttribute("data", userDTO);
//            }
//
//        }
//
//        return null;
//    }
//
//
//    @PostMapping("user/update/{id}")
//    public String submitRegisterForm(@Valid @ModelAttribute UpdateUserRequestDTO updateUserDTO,
//                                     HttpServletRequest httpServletRequest,
//                                     RedirectAttributes redirectAttributes)
//            throws IOException, InterruptedException {
//        Cookie[] cookies = httpServletRequest.getCookies();
//
//        if (cookies == null) {
//            return "user/access-denied.html";
//        }
//
//        for (Cookie cookie : cookies) {
//            if (!("jwtToken".equals(cookie.getName()))) {
//                continue;
//            } else {
//                try {
//                } catch (Exception e) {
//                    System.out.println("Caught an exception: " + e.getMessage());
//                    e.printStackTrace();
//                    return "error";
//                }
//                return "redirect:profile/profile.html";
//
//            }
//        }
//        return null;
//    }

// User Service Get by ID
//    public ResponseEntity<UserDTO> getUserById(String id) {
//        return restTemplate.exchange(
//                URL_USER_SERVICE + id,
//                HttpMethod.GET,
//                null,
//                UserDTO.class);
//    }

//  User Service Update
//    public ResponseEntity<UserDTO> updateUserById(String id, UpdateUserRequestDTO requestDTO) {
//
//        return restTemplate.exchange(URL_USER_SERVICE + "update/" + id,
//                HttpMethod.PUT,
//                new HttpEntity<>(requestDTO),
//                UserDTO.class
//        );
//    }


//    @GetMapping("user/update/{id}")
//    public String getUpdateForm(@PathVariable String id,
//                                  Model model,
//                                  HttpServletRequest httpServletRequest,
//                                  RedirectAttributes redirectAttributes
//    ) throws io.jsonwebtoken.io.IOException, InterruptedException{