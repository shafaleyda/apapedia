// package com.apapedia.order.model;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.NotNull;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// import java.util.UUID;

// import com.fasterxml.jackson.annotation.JsonIgnore;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Entity
// @Table(name = "order_item")
// public class OrderItemModel {
//     @Id
//     @NotNull
//     @Column(name = "product_id", nullable = false)
//     private UUID productId;

//     @NotNull
//     @Column(name = "quantity", nullable = false)
//     private int quantity;

//     @NotNull
//     @Column(name = "product_name", nullable = false)
//     private String productName;

//     @NotNull
//     @Column(name = "product_price", nullable = false)
//     private int productPrice;

//     @ManyToOne
//     @JsonIgnore
//     @JoinColumn(name = "order_id", nullable = false)
//     private OrderModel order;
// }

package com.apapedia.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItemModel {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private int productPrice;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;
}

