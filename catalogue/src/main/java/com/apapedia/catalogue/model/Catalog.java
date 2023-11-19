package com.apapedia.catalogue.model;

import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.usertype.UserType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE catalog SET is_deleted = true WHERE id_catalog = ?")
@Where(clause = "is_deleted = false")
@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_catalog")
    private UUID idCatalog = UUID.randomUUID();

    @NotNull
    @GeneratedValue(generator = "UUID")
    @Column(name = "seller")
    private UUID seller;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "id_category")
    private Category category;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Lob
    @Column(name = "image")
    private String image;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;
}
