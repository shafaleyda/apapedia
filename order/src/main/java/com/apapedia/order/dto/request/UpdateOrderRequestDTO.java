package com.apapedia.order.dto.request;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequestDTO extends CreateOrderRequestDTO{
    @NotNull
    private UUID id;
}
