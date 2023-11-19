package com.apapedia.order.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_item")
public class OrderItemModel {

    @Id
    @NotNull
    @Size(max = 100)
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @NotNull
    @Size(max = 100)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Size(max = 100)
    @Column(name = "product_name", nullable = false)
    private Integer productName; 

    @NotNull
    @Size(max = 100)
    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderModel order;
}
