package com.apapedia.catalogue.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

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
    @Column(name = "id_catalog")
    private UUID idCatalog = UUID.randomUUID();

    @NotNull
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
    @Column(name = "image", length = 1000)
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;


    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "seller", referencedColumnName = "id_user")
    // private User seller;


    //    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
//    @JoinTable(name="image", joinColumns={@JoinColumn(name="id_catalog")},inverseJoinColumns={@JoinColumn(name="id_image")})
//    private Set<ImageData> image;
}
