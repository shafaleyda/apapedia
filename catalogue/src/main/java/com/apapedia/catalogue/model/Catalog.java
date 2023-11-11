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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Blob;

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

    //seller

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
    @JsonBackReference
    private Category category;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock; 

    @NotNull
    @Column(name = "image", length = 1000)
    @Basic(fetch = FetchType.EAGER)
    @Lob
    private byte[] image;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;
}
