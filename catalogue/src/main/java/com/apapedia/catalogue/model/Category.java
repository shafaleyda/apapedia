package com.apapedia.catalogue.model;

import java.util.UUID;
import java.util.List;

import com.apapedia.catalogue.model.Catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id_category")
    private UUID idCategory = UUID.randomUUID();

    @NotNull
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "categoryId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Catalog> listCatalog;
}
