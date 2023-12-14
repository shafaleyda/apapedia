package com.apapedia.frontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private int balance;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;
    private String category;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private List<Authority> authorities;
    private boolean enabled;

    // Constructors, getters, and setters
}