package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.model.Image;
import com.apapedia.catalogue.restservice.CatalogRestService;
//import com.apapedia.catalogue.service.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private CatalogRestService catalogRestService;
//
//    @Autowired
//    private StorageService service;

    @GetMapping(value="/catalog/{idCatalog}")
    private Catalog retrieveCatalogue(@PathVariable("idCatalog") UUID idCatalog){
        try{
            return catalogRestService.getRestCatalogById(idCatalog);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog  No " + String.valueOf(idCatalog) + " Not Found."
            );
        }
    }

    @GetMapping(value="/catalog/seller/{idSeller}")
    private Catalog retrieveCatalogueBySellerId(@PathVariable("idSeller") Long idSeller){
        try{
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog  No " + String.valueOf(idSeller) + " Not Found."
            );
        }
        return null;
    }

    @GetMapping(value="/catalog/all")
    private List<Catalog> retrieveAllCatalogue(){
        return catalogRestService.retrieveRestAllCatalog();

    }

//    @PostMapping(value={"/catalog/create"}, produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Catalog createRestCatalogue(@Valid @RequestBody CreateCatalogueRequestDTO catalogDTO,
                                       BindingResult bindingResult,
                                       @RequestPart("imageFile") MultipartFile[] imageFiles)
                                        throws IOException
                                       {
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            // Upload images and create a catalog
            Set<Image> imageModel = new HashSet<>();
            for (MultipartFile file : imageFiles) {
                Image imageModels = new Image(
                        file.getOriginalFilename(), file.getContentType(), file.getBytes());
                imageModel.add(imageModels);
            }
            var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
            catalogRestService.createRestCatalog(catalog);
            return catalog;
        }
    }

//    @PostMapping
//    public Set<Image> uploadImage(MultipartFile[] multipartFiles) throws IOException {
////        String uploadImage = service.uploadImage(file);
////        return ResponseEntity.status(HttpStatus.OK)
////                .body(uploadImage);
//        Set<Image> imageModel = new HashSet<>();
//        for (MultipartFile file: multipartFiles){
//            Image imageModels = new Image(
//                file.getOriginalFilename(), file.getContentType(), file.getBytes());
//            imageModel.add(imageModels);
//        }
//        return imageModel;
//    }

    @PutMapping(value = "/catalog/{idCatalog}")
    private Catalog updateRestPenerbit(
            @PathVariable("idCatalog") UUID idCatalog,
            @RequestBody UpdateCatalogRequestDTO updateCatalogRequestDTO) {
        try {
            return catalogRestService.updateCatalog(idCatalog, updateCatalogRequestDTO);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog No" + idCatalog + " Not Found!"
            );

        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, " "
            );
        }
    }

//    @GetMapping("/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//        byte[] imageData=service.downloadImage(fileName);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageData);
//
//    }

}

