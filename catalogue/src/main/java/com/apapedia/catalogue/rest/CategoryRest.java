package com.apapedia.catalogue.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRest {
    private Integer idCategory;
    private String categoryName; 
}
