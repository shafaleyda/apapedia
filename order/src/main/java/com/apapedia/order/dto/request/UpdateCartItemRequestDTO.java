package com.apapedia.order.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCartItemRequestDTO extends CreateCartItemRequestDTO{
    private UUID id;

    private Integer quantity;
}
