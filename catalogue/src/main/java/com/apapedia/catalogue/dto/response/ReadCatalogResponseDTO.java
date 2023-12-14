package com.apapedia.catalogue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReadCatalogResponseDTO {
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
