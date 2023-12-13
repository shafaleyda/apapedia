package com.apapedia.frontend.controller;

import com.apapedia.frontend.dto.request.UpdateUserRequestDTO;
import com.apapedia.frontend.dto.response.UserDTO;

//import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.setting.Setting;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
//        } catch (Exception e) {
//            System.out.println("Caught an exception: " + e.getMessage());
//            e.printStackTrace();
//            return "error";
//        }
//            }

//        }
//        return "user/access-denied.html";
    }

    // ----------- UPDATE -----------

//    @GetMapping("user/update/{id}")
//    public String updatePage(@PathVariable String id, Model model) {
//        var userDto = userService.getUserById(id);
//
//        if (userDto.getStatusCode().is2xxSuccessful()) {
//            model.addAttribute("data", userDto.getBody());
//            return "profile/update-profile";
//        } else {
//            return "error";
//        }
//    }
//
//    @PostMapping("user/update/{id}")
//    public String update(@ModelAttribute("data") UpdateUserRequestDTO updateUserRequestDTO,
//                         @PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            userService.updateUserById(id, updateUserRequestDTO);
//            redirectAttributes.addFlashAttribute("message", " Success");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//            return "redirect:/user/profile";
//        } catch (HttpClientErrorException e){
//            redirectAttributes.addFlashAttribute("message", " Failed");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
//            return redirectError(e.getStatusCode());
//        }
//
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
////        Cookie[] cookies = httpServletRequest.getCookies();
////
////        if (cookies == null) {
////            return "user/access-denied.html";
////        }
////
////        for (Cookie cookie : cookies) {
////            if (!("jwtToken".equals(cookie.getName()))) {
////                continue;
////            } else{
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
//                        Object data = sellerResponse.getBody();
//
//                        Map<String, Object> userLoggedIn = sellerResponse.getBody();
//                        String sellerId = (String) userLoggedIn.get("id");
//
//
//                        ResponseEntity<UserDTO> response = restTemplate.exchange(
//                                url,
//                                HttpMethod.GET,
//                                null,
//                                new ParameterizedTypeReference<UserDTO>() {
//                                }
//                                );
//                        model.addAttribute("data", data);
//                        final String urlGetId = "http://localhost:8081/api/user/" + sellerId;
//
//                        ResponseEntity<String> getIdResponse = restTemplate.exchange(
//                                urlGetId,
//                                HttpMethod.GET,
//                                null,
//                                String.class
//                        );
//
//                        if (getIdResponse.getStatusCode().is2xxSuccessful()) {
//                            Object responseBody = sellerResponse.getBody();
//                            model.addAttribute("data", responseBody);
//
//
//                        }
//
//                            return "profile/update-profile";
//                    } else {
//                        return "error";
//                    }
//                } catch (Exception e) {
//                    System.out.println("Caught an exception: " + e.getMessage());
//                    e.printStackTrace();
//                    return "error";
//                }
////            }
////        }
////
////        return "user/access-denied.html";
//    }

//    @PostMapping("user/update/{id}")
//    public String submitUpdateForm(@PathVariable String id,
//                                Model model,
//                                HttpServletRequest httpServletRequest,
//                                RedirectAttributes redirectAttributes,
//                                UpdateUserRequestDTO updateDTO
//    ) throws io.jsonwebtoken.io.IOException, InterruptedException{
//        return null;
//    }


    // ----------- DELETE -----------
//    @GetMapping("user/delete/{id}")
//    public String delete(@PathVariable String id, Model mode, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//        try{
//            userService.deleteUserById(id);
//            userService.deleteJwtTokenCookie(response);
//            redirectAttributes.addFlashAttribute("message", " Success Delete");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
//            return "redirect:/";
//        }catch (HttpClientErrorException e){
//            redirectAttributes.addFlashAttribute("message", " Failed Delete");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
//            return redirectError(e.getStatusCode());
//        }
//    }

    @DeleteMapping("user/delete")
    public String deleteAccount(
                                  Model model,
                                  HttpServletRequest httpServletRequest,
                                  RedirectAttributes redirectAttributes
    ) throws io.jsonwebtoken.io.IOException, InterruptedException{
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

//    @DeleteMapping("user/delete/{id}")
//    public String deleteAccount(@PathVariable String id, Model model, HttpServletRequest httpServletRequest)
//            throws IOException, InterruptedException {
////        try {
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
//                    String urlLoggedIn = "http://localhost:8081/api/user/user-loggedin";
//                    ResponseEntity<Map<String, Object>> responseLoggedIn =
//                            restTemplate.exchange(
//                                    urlLoggedIn,
//                                    HttpMethod.GET,
//                                    null,
//                                    new ParameterizedTypeReference<Map<String, Object>>() {
//                                    }
//                            );
//
//                    if (responseLoggedIn.getStatusCode().is2xxSuccessful()) {
//                        Object responseBody = responseLoggedIn.getBody();
//                        Map<String, Object> response = responseLoggedIn.getBody();
//                        String seller = (String) response.get("id");
//
//                        // Delete the user account using the correct URL
//                        String urlDelete = "http://localhost:8081/api/user/delete/" + seller;
//
//                        ResponseEntity<String> deleteResponse =
//                                restTemplate.exchange(
//                                        urlDelete,
//                                        HttpMethod.DELETE,
//                                        null,
//                                        String.class
//                                );
//
//                        if (deleteResponse.getStatusCode().is2xxSuccessful()) {
//                            String deleteResponseBody = deleteResponse.getBody();
//                            model.addAttribute("message", deleteResponseBody);
//                            System.out.println(deleteResponseBody);
//                            return "successful";
//                        }
//                    } else {
//                        return "error";
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("Caught an exception: " + e.getMessage());
//                    e.printStackTrace();
//                    return "error";
//                }
//            }
//        }
//        return "user/access-denied.html";
//
//    }
//
//    @DeleteMapping("user/delete")
//    public String deleteAccount(Model model) throws IOException, InterruptedException {
//        RestTemplate restTemplate = new RestTemplate();
//        String urlLoggedIn = "http://localhost:8081/api/user/user-loggedin";
//
//        ResponseEntity<Map<String, Object>> responseLoggedIn =
//                restTemplate.exchange(
//                        urlLoggedIn,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<Map<String, Object>>() {}
//                );
//
//        if (responseLoggedIn.getStatusCode().is2xxSuccessful()) {
//            Map<String, Object> response = responseLoggedIn.getBody();
//            String seller = (String) response.get("id");
//
//            String urlDelete = "http://localhost:8081/api/user/delete/" + seller;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            ResponseEntity<Map<String, Object>> deleteResponse =
//                    restTemplate.exchange(
//                            urlDelete,
//                            HttpMethod.DELETE,
//                            null,
//                            new ParameterizedTypeReference<Map<String, Object>>() {}
//                    );
//
//            if (deleteResponse.getStatusCode().is2xxSuccessful()) {
//                return "successful";
//            } else {
//                // Log error or handle unsuccessful DELETE response
//                System.err.println("DELETE request was not successful. Response code: " + deleteResponse.getStatusCodeValue());
//            }
//        } else {
//            // Log error or handle unsuccessful GET response
//            System.err.println("GET request to /api/user/user-loggedin was not successful. Response code: " + responseLoggedIn.getStatusCodeValue());
//        }
//
//        // Handle the case where the execution did not reach the successful return statement
//        return "error";
//    }

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
