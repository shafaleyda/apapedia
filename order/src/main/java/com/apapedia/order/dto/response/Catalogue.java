package com.apapedia.order.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Catalogue {
    @JsonProperty("idCatalog")
    private UUID idCatalog;

    @JsonProperty("seller")
    private UUID seller;

    @JsonProperty("price")
    private int price;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productDescription")
    private String productDescription;

    @JsonProperty("categoryId")
    private int categoryId;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("stock")
    private int stock;

    @JsonProperty("image")
    private String image;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}