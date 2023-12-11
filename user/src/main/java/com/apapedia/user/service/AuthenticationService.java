package com.apapedia.user.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.RegisterCustomerRequestDTO;
import com.apapedia.user.dto.request.RegisterSellerRequestDTO;
import com.apapedia.user.dto.response.AuthenticationResponse;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.config.JwtService;
import lombok.RequiredArgsConstructor;


import static com.apapedia.user.model.Role.CUSTOMER;
import static com.apapedia.user.model.Role.SELLER;;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerDb custDb;
    private final SellerDb sellerDb;
    private final UserDb userDb;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerCustomer(RegisterCustomerRequestDTO request) throws Exception {

        boolean usernameExists = userDb.existsByUsername(request.getUsername());
        boolean emailExists = userDb.existsByEmail(request.getEmail());

        if (usernameExists || emailExists) {
            throw new Exception("User already registered");
        }

        var user = Customer.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .balance(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .cartId(UUID.randomUUID())
                .role(CUSTOMER)
                .build();
        custDb.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse registerSeller(RegisterSellerRequestDTO request)
            throws Exception {

        boolean usernameExists = userDb.existsByUsername(request.getUsername());
        boolean emailExists = userDb.existsByEmail(request.getEmail());

        if (usernameExists || emailExists) {
            throw new Exception("User already registered");
        }

        var user = Seller.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .username(request.getUsername())
                .address(request.getAddress())
                .balance(0)
                .password(passwordEncoder.encode("dummypass"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(SELLER)
                .category(request.getCategory())
                .build();

        sellerDb.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authReq) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReq.getEmail(),
                        authReq.getPassword()));

        var user = userDb.findByEmail(authReq.getEmail()).orElseThrow(() -> new Exception("User not found."));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
