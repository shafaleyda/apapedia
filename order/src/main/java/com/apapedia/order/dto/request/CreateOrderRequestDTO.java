package com.apapedia.order.dto.request;

import java.util.UUID;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CreateOrderRequestDTO {
    private HashMap<UUID,Integer> items;

    private UUID customer;

    private int status;
}
