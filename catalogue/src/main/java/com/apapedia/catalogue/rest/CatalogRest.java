package com.apapedia.catalogue.rest;

import com.apapedia.catalogue.model.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CatalogRest {
    private UUID idCatalog;
    private UUID seller;
    private Integer price;
    private String productName;
    private String productDescription;
    private Integer categoryId;
    private String categoryName;
    private Integer stock;
    private String image;
    private Boolean isDeleted;
}