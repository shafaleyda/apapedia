package com.apapedia.catalogue.dto.request;

import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.model.Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCatalogueRequestDTO {

    private String productName;

    private int productPrice;

    private String productDescription;

    private int stock;

    private Set<Image> image;

    private Category categoryName;
}