package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class OrderModel {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @NotNull
    @Column(name = "customer", nullable = false)
    private UUID customer;

    @NotNull
    @Column(name = "seller", nullable = false)
    private UUID seller;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderItemModel> ListOrderItem;
}
