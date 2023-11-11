package com.apapedia.catalogue.dto.mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import org.mapstruct.Mapper;

import com.apapedia.catalogue.model.Catalog;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    Catalog createCatalogRequestDTOToCartModel(CreateCatalogueRequestDTO createCatalogRequestDTO);
}