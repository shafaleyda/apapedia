package com.apapedia.frontend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
