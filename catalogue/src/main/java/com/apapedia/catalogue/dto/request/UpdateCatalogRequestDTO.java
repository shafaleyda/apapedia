package com.apapedia.catalogue.dto.request;

import com.apapedia.catalogue.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCatalogRequestDTO extends CreateCatalogueRequestDTO{
    private UUID idCatalog;

    @NotBlank
    private String productName;

    @NotBlank
    private int productPrice;

    @NotBlank
    private String productDescription;

    @NotBlank
    private int stock;

    @NotBlank
    private String image;

    private Category categoryName;
}
