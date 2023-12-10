package com.apapedia.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerRequestDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
}
