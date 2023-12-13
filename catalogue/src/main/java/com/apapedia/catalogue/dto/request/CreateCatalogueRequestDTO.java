package com.apapedia.catalogue.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCatalogueRequestDTO {
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