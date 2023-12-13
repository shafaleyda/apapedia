package com.apapedia.catalogue.dto.mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;

import org.mapstruct.Mapper;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;


@Mapper(componentModel = "spring")
public interface CatalogMapper {
    Catalog createCatalogRequestDTOToCatalogModel(CreateCatalogueRequestDTO createCatalogRequestDTO);
    CreateCatalogueRequestDTO createCatalogRequestDTOToCatalogModel(CatalogRest catalogRest);
    ReadCatalogResponseDTO catalogRestToReadCatalogResponseDTO(CatalogRest catalogRest);
}
