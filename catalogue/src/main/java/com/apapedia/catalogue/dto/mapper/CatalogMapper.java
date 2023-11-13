package com.apapedia.catalogue.dto.mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
//import com.apapedia.catalogue.model.ImageData;
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

//        // Custom mapping method for handling byte[] to Set<ImageData> conversion
//        @Named("mapImage")
//        default Set<ImageData> mapImage(byte[][] image) {
//            Set<ImageData> imageSet = new HashSet<>();
//            ImageData imageModel = new ImageData();
//            imageModel.setImageData(image); // Assuming you have a method to set imageData in your ImageData entity
//            imageSet.add(imageModel);
//            return imageSet;
//        }
    }