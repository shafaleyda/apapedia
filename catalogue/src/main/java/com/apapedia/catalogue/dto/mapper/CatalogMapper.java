
//****------------------------NOT USED FOR NOW------------------------****??


package com.apapedia.catalogue.dto.mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;

//import com.apapedia.catalogue.model.ImageData;
import org.mapstruct.Mapper;

    import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
    public interface CatalogMapper {
        Catalog createCatalogRequestDTOToCatalogModel(CreateCatalogueRequestDTO createCatalogRequestDTO);
        ReadCatalogResponseDTO catalogRestToReadCatalogResponseDTO(CatalogRest catalogRest); 
    }