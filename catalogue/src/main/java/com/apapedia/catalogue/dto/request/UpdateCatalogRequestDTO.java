package com.apapedia.catalogue.dto.request;

import com.apapedia.catalogue.model.Category;
//import com.apapedia.catalogue.model.ImageData;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCatalogRequestDTO{
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
