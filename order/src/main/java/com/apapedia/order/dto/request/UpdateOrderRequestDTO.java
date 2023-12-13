package com.apapedia.order.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequestDTO extends CreateOrderRequestDTO{
    private UUID id;

    private int status;
}
