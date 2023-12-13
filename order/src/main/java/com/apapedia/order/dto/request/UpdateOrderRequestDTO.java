package com.apapedia.order.dto.request;

// import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequestDTO extends CreateOrderRequestDTO{
    private UUID id;

    @Min(value = 0, message = "Status must be a non-negative integer")
    private int status;
}
