package com.apapedia.catalogue.dto;

import org.mapstruct.Mapper;

import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    ReadCatalogResponseDTO catalogToReadCatalogResponseDTO(Catalog catalog); 
}
