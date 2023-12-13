package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class CartModel {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice = 0;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItemModel> listCartItem;

    // user yang bisa berbelanja hanyalah customer
    // @OneToOne(cascade = CascadeType.ALL) 
    // @JoinColumn(name = "customer_id", referencedColumnName = "id")
    // private UUID customerId; 
}
