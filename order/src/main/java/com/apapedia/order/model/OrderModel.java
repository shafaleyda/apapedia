package com.apapedia.order.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="order")
public class OrderModel {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Size(max = 100)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotNull
    @Size(max = 100)
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @NotNull
    @Column(name = "customer", nullable = false)
    private UUID customer;

    @NotNull
    @Column(name = "seller", nullable = false)
    private UUID seller;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItemModel> listOrderItem;

    // asumsi berhubungan dengan tabel user
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "id_customer", referencedColumnName = "id")
    // private UUID customer;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "id_seller", referencedColumnName = "id")
    // private UUID seller;

    // untuk di user
    // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Order> listOrder;

    // @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Order> listOrder;
}
