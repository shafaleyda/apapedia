package com.apapedia.catalogue.dto.mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Image;
import org.mapstruct.Mapper;

    import com.apapedia.catalogue.model.Catalog;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
    public interface CatalogMapper {
//        @Mappings({
//                @Mapping(target = "image", source = "createCatalogRequestDTO.image", qualifiedByName = "mapImage")
//        })
        Catalog createCatalogRequestDTOToCatalogModel(CreateCatalogueRequestDTO createCatalogRequestDTO);

//        // Custom mapping method for handling byte[] to Set<Image> conversion
//        @Named("mapImage")
//        default Set<Image> mapImage(byte[][] image) {
//            Set<Image> imageSet = new HashSet<>();
//            Image imageModel = new Image();
//            imageModel.setImageData(image); // Assuming you have a method to set imageData in your Image entity
//            imageSet.add(imageModel);
//            return imageSet;
//        }
    }