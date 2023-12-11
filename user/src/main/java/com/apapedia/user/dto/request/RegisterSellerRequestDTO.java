package com.apapedia.user.dto.request;

import com.apapedia.user.model.SellerCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterSellerRequestDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private String address;
    private SellerCategory category;
}
