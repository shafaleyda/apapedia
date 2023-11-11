package com.apapedia.catalogue.dto.request;

import com.apapedia.catalogue.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCatalogueRequestDTO {

    private String productName;

    private int productPrice;

    private String productDescription;

    private int stock;

    private String image;

    private Category categoryName;
}