package com.apapedia.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartItemRequestDTO {
    private UUID productId;
    private Integer quantity;

}
