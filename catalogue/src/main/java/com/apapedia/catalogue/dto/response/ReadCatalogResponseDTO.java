package com.apapedia.catalogue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.apapedia.catalogue.model.Category;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadCatalogResponseDTO {
    private UUID idCatalog; 
    private Integer price;
    private String productName; 
    private String productDescription; 
    private Category categoryId; 
    private Integer stock; 
    private byte[] image; 
    private Boolean isDeleted; 
}
