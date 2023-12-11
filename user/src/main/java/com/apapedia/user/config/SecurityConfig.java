package com.apapedia.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static com.apapedia.user.model.Role.SELLER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static com.apapedia.user.model.Permission.SELLER_CREATE;
import static com.apapedia.user.model.Permission.SELLER_DELETE;
import static com.apapedia.user.model.Permission.SELLER_READ;
import static com.apapedia.user.model.Permission.SELLER_UPDATE;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;
    
    private static final String[] WHITE_LIST_URL = {


        //USER SERVICE
        "/**",
        "/",
        "/api/authentication/**",
        "/api/auth/**",

        "/api/user/**",

        "/validate-ticket",
        "/login-sso",
        "/logout-sso",
        "/failed-register",
        "/failed-login",
        "/dashboard/seller/guest",
        "/dashboard/seller",
        "/register/seller",
        "/api/catalog/view-all-by-name",
        "/api/catalog/all",
        "/api/user/**"
    };

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        //Disini masukkin URL yang perlu keamanan
                        .requestMatchers("/api/v1/admin/**").hasAnyRole(SELLER.name())
                        .requestMatchers(GET, "/api/v1/admin/**").hasAnyAuthority(SELLER_READ.name())
                        .requestMatchers(POST, "/api/v1/admin/**").hasAnyAuthority(SELLER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/admin/**").hasAnyAuthority(SELLER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/admin/**").hasAnyAuthority(SELLER_DELETE.name())
                        
                        //View all catalog (role seller)
                        .requestMatchers(GET, "/api/catalog/seller-view-all-by-name").hasRole("SELLER")
                        .requestMatchers(POST, "/api/catalog/create").hasRole("SELLER")
                        .requestMatchers(GET, "/api/catalog/seller/**").hasRole("SELLER")
                        .requestMatchers(GET, "/api/catalog/seller-view-all-by-price").hasRole("SELLER")
                        .requestMatchers(GET, "/api/catalog/seller-view-all-sort-by").hasRole("SELLER")
                        .anyRequest()
                        .authenticated())
                        .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("http://localhost:8085/")
                        )   
                        
                        

        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
