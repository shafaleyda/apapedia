package com.apapedia.order.dto.request;

import java.util.UUID;

import org.springframework.cglib.core.Local;

import com.apapedia.order.model.OrderItemModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
    // private HashMap<UUID,Integer> items;

    // private UUID customer;

    private LocalDateTime updatedAt;

    private Integer status;

    // private Integer totalPrice;

    // private UUID seller;
}
