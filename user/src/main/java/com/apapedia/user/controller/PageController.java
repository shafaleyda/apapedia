package com.apapedia.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.apapedia.user.config.JwtService;
import com.apapedia.user.restservice.UserRestService;
import com.apapedia.user.security.xml.Attributes;
import com.apapedia.user.security.xml.ServiceResponse;
import com.apapedia.user.setting.Setting;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
        @Autowired
        private JwtService jwtService;

        @Autowired
        private UserRestService userRestService;

        private WebClient webClient = WebClient.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                        .jaxb2Decoder(new Jaxb2XmlDecoder()))
                        .build();

        @GetMapping("/validate-ticket")
        public ModelAndView adminLoginSSO(@RequestParam(value = "ticket", required = false) String ticket,
                        HttpServletRequest request, HttpServletResponse httpResponse) {
                System.out.println("Masuk validate ticket");

                ServiceResponse serviceResponse = this.webClient.get().uri(
                                String.format(
                                                Setting.SERVER_VALIDATE_TICKET,
                                                ticket,
                                                Setting.CLIENT_LOGIN))
                                .retrieve().bodyToMono(ServiceResponse.class).block();

                Attributes attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
                String username = serviceResponse.getAuthenticationSuccess().getUser();

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, "webadmin", null);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);

                String name = attributes.getNama();

                System.out.println("Masuk validate ticket bozz");

                try {
                        System.out.println("Masuk validate ticket 2");
                        var token = userRestService.getToken(username, name);

                        System.out.println("Masuk validate ticket 3");
                        HttpSession httpSession = request.getSession(true);
                        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                        securityContext);
                        httpSession.setAttribute("token", token);

                        System.out.println("MASUK SINI");

                        Cookie cookie = new Cookie("jwtToken", token);
                        cookie.setPath("/");
                        cookie.setSecure(true);
                        cookie.setMaxAge(24 * 60 * 60); // 24 hours in seconds

                        // Add the cookie to the response
                        httpResponse.addCookie(cookie);

                        return new ModelAndView("redirect:http://localhost:8085/dashboard/seller");

                } catch (Exception e) {
                        // TODO: handle exception

                        var token = jwtService.getToken();
                        if (token != null) {
                                var idUser = jwtService.extractUserId(token);
                                userRestService.deleteUser(idUser);
                        }

                        return new ModelAndView("redirect:http://localhost:8085/failed-login");

                }

        }

        @GetMapping("/login-sso")
        public ModelAndView loginSSO() {
                System.out.println("masuk login user");
                return new ModelAndView("redirect:" + Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
        }

        @GetMapping("/logout-sso")
        public ModelAndView logoutSSO(Principal principal, HttpServletResponse response) {
                Cookie cookie = new Cookie("jwtToken", null);
                cookie.setPath("/");
                cookie.setMaxAge(0); // Set masa berlaku cookie menjadi 0 untuk menghapusnya

                // Menambahkan cookie yang sama dengan nilai null dan masa berlaku yang sudah
                // lewat
                response.addCookie(cookie);

                return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
        }

        @GetMapping("/failed-login")
        public String failedLogin() {

                return "failed-login.html";
        }

}
