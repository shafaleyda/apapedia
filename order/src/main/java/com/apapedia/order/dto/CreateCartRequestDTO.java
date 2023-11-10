package com.apapedia.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartRequestDTO {
    @NotBlank
    private UUID userId;

    @NotBlank
    private Long totalPrice;
}
