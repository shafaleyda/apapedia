package com.apapedia.order.dto.request;

import java.util.UUID;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequestDTO {
    private HashMap<UUID,Integer> items;
    private UUID customer;
}
