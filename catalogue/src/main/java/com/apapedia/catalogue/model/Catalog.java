package com.apapedia.catalogue.model;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE catalog SET is_deleted = true WHERE id_catalog = ?")
@Where(clause = "is_deleted = false")
@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @Column(name = "id_catalog")
    private UUID idCatalog = UUID.randomUUID();

    //seller

    @NotNull
    @Column(name = "price", nullable = false)
    private int productPrice;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id_category")
    private Category categoryId;

    @NotNull
    @Column(name = "stock", nullable = false)
    private int stock;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="image", joinColumns={@JoinColumn(name="id_catalog")},inverseJoinColumns={@JoinColumn(name="id_image")})
    private Set<Image> image;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;
}
