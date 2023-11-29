package com.apapedia.user.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.RegisterRequest;
import com.apapedia.user.dto.response.AuthenticationResponse;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.config.JwtService;
import lombok.RequiredArgsConstructor;

import static com.apapedia.user.model.Role.CUSTOMER;;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerDb custDb;
    private final SellerDb sellerDb;
    private final UserDb userDb;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws Exception {

        var checkUser = userDb.findByEmail(registerRequest.getEmail());
        if (checkUser == null) {
            throw new Exception("User already registered.");
        }

        if (registerRequest.getRole().equals(CUSTOMER)) {
            System.out.println("MASUK");
            
            var user = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .address(registerRequest.getAddress())
                    .balance(0)                    
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .cartId(UUID.randomUUID())
                    .role(registerRequest.getRole())           
                    .build();
            custDb.save(user);
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            var user = Seller.builder()
                    .id(UUID.randomUUID())
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .username(registerRequest.getUsername())
                    .address(registerRequest.getAddress())
                    .balance(0)
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .role(registerRequest.getRole())
                    .category(registerRequest.getCategory())
                    .build();

            sellerDb.save(user);
            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder().token(jwtToken).build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authReq) throws Exception {
        try {
            var user = userDb.findByEmail(authReq.getEmail()).orElseThrow(() -> new Exception("User not found."));

            if (passwordEncoder.matches(authReq.getPassword(), user.getPassword())) {
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder().token(jwtToken).build();
            }
            
        } catch(AuthenticationException ex){
            System.out.println(ex.getMessage());
        }
        return null;

    }
}
