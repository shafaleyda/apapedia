package com.apapedia.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.apapedia.user.user.Permission.SELLER_CREATE;
import static com.apapedia.user.user.Permission.SELLER_DELETE;
import static com.apapedia.user.user.Permission.SELLER_READ;
import static com.apapedia.user.user.Permission.SELLER_UPDATE;
import static com.apapedia.user.user.Permission.CUSTOMER_CREATE;
import static com.apapedia.user.user.Permission.CUSTOMER_DELETE;
import static com.apapedia.user.user.Permission.CUSTOMER_READ;
import static com.apapedia.user.user.Permission.CUSTOMER_UPDATE;
import static com.apapedia.user.user.Role.SELLER;
import static com.apapedia.user.user.Role.CUSTOMER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
<<<<<<< HEAD
<<<<<<< HEAD
                                // .requestMatchers("/api/v1/management/**").hasAnyRole(SELLER.name(), CUSTOMER.name())
                                // .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(SELLER_READ.name(), CUSTOMER_READ.name())
                                // .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(SELLER_CREATE.name(), CUSTOMER_CREATE.name())
                                // .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(SELLER_UPDATE.name(), CUSTOMER_UPDATE.name())
                                // .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(SELLER_DELETE.name(), CUSTOMER_DELETE.name())
=======
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d
                                 .requestMatchers("/api/v1/management/**").hasAnyRole(SELLER.name(), CUSTOMER.name())
                                 .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(SELLER_READ.name(), CUSTOMER_READ.name())
                                 .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(SELLER_CREATE.name(), CUSTOMER_CREATE.name())
                                 .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(SELLER_UPDATE.name(), CUSTOMER_UPDATE.name())
                                 .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(SELLER_DELETE.name(), CUSTOMER_DELETE.name())
<<<<<<< HEAD
>>>>>>> 3df004dc477b2fcdf40967613090d373b77d4980
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }
}