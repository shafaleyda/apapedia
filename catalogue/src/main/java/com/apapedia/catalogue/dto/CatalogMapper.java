package com.apapedia.catalogue.dto;

import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.restservice.CatalogRestService;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    
    ReadCatalogResponseDTO catalogToReadCatalogResponseDTO(Catalog catalog); 

    // @AfterMapping
    // default void setImageData(Catalog catalog, @MappingTarget ReadCatalogResponseDTO readCatalogResponseDTO) {
    //     if (catalog.getImage() != null && catalog != null) {
    //         readCatalogResponseDTO.setImage();
    //     }
    // }
}
