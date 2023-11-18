package com.apapedia.user.auth;

import com.apapedia.user.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
    private Role role;
}