package com.apapedia.user.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.RegisterCustomerRequestDTO;
import com.apapedia.user.dto.request.RegisterSellerRequestDTO;
import com.apapedia.user.dto.response.AuthenticationResponse;
import com.apapedia.user.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService service;

    
    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> registerCustomer(
        @RequestBody RegisterCustomerRequestDTO request
    ) throws Exception {
        return ResponseEntity.ok(service.registerCustomer(request));
    }

    @PostMapping("/register/seller")
    public ResponseEntity<AuthenticationResponse> registerSeller(
        @RequestBody RegisterSellerRequestDTO request
    ) throws Exception {
        return ResponseEntity.ok(service.registerSeller(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) throws Exception {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
