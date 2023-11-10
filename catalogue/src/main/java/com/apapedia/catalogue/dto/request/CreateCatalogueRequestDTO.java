package com.apapedia.catalogue.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCatalogueRequestDTO {
    private String namaPenerbit;
    private String alamat;
    private String email;
}