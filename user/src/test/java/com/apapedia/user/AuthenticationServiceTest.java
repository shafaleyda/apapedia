package com.apapedia.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import com.apapedia.user.config.JwtService;
import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.RegisterCustomerRequestDTO;
import com.apapedia.user.dto.request.RegisterSellerRequestDTO;
import com.apapedia.user.dto.response.AuthenticationResponse;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Role;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.SellerCategory;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.AuthenticationService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomerDb custDb;

    @Mock
    private SellerDb sellerDb;

    @Mock
    private SellerCategory sellerCategory;

    @Mock
    private UserDb userDb;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private JwtService jwtService;

    @Test
    public void testRegisterCustomer() throws Exception {
        // Mock request
        RegisterCustomerRequestDTO request = new RegisterCustomerRequestDTO();
        // Populate request here
        request.setName("John Doe"); // Set the name
        request.setEmail("john@example.com"); // Set the email
        request.setUsername("rangga.yudhistira"); // Set the username
        request.setPassword("password"); // Set the password
        request.setAddress("123 Main St");

        // Mocking behavior
        when(userDb.existsByUsername(request.getUsername())).thenReturn(false);
        when(userDb.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        // Call the method to be tested
        AuthenticationResponse response = authService.registerCustomer(request);

        // Verify behavior
        verify(custDb).save(any(Customer.class));
        // Add more assertions based on the expected behavior
        assertNotNull(response); // Verify that the response is not null

    }

    @Test
    public void testRegisterSeller() throws Exception {
        // Mock request
        RegisterSellerRequestDTO request = new RegisterSellerRequestDTO();
        // Populate request here
        request.setName("John Doe"); // Set the name
        request.setEmail("john@example.com"); // Set the email
        request.setUsername("rangga.yudhistira"); // Set the username
        request.setPassword("password"); // Set the password
        request.setAddress("123 Main St");
        request.setCategory(sellerCategory.BIASA);

        // Mocking behavior
        when(userDb.existsByUsername(request.getUsername())).thenReturn(false);
        when(userDb.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Call the method to be tested
        AuthenticationResponse response = authService.registerSeller(request);

        // Verify behavior
        verify(sellerDb).save(any(Seller.class));
        // Add more assertions based on the expected behavior
        assertNotNull(response); // Verify that the response is not null
    }

}
