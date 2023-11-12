package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;
//import com.apapedia.catalogue.model.ImageData;
import com.apapedia.catalogue.restservice.CatalogRestService;
//import com.apapedia.catalogue.service.StorageService;
import com.apapedia.catalogue.service.StorageService;
import com.apapedia.catalogue.utils.FileStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private CatalogRestService catalogRestService;

    @Autowired
    private StorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

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
    private List<ReadCatalogResponseDTO> retrieveAllCatalogue(){
        return catalogRestService.retrieveRestAllReadCatalogResponseDTO();

    }
//
//    @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public Catalog createRestCatalogue(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogDTO,
//                                       BindingResult bindingResult,
//                                       @RequestParam("image") MultipartFile[] imageFiles)
//            throws IOException {
//
//            // Upload images and create a catalog
//            var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
//            catalogRestService.createRestCatalog(catalog, imageFiles);
//            return catalog;
//
//    }

    @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Catalog createRestCatalogue(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogDTO,
                                       BindingResult bindingResult,
                                       @RequestParam("image") MultipartFile[] imageFiles,
                                       @RequestParam("model") String jsonObject)
            throws IOException {

        // Upload images and create a catalog
        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
        // Handle file uploads
        List<String> fileNames = Arrays.stream(imageFiles)
                .map(file -> {
                    return fileStorageService.storeFile(file);
                })
                .collect(Collectors.toList());
        String concatenatedFileNames = String.join(",", fileNames);
        byte[] fileNamesBytes = concatenatedFileNames.getBytes(StandardCharsets.UTF_8);

        // Attach image file names to the catalog
        catalog.setImage(fileNamesBytes);


        // Continue with your existing code to save the catalog
        catalogRestService.createRestCatalog(catalog, imageFiles, jsonObject);

        return catalog;

    }

    @PutMapping(value = "/catalog/{idCatalog}")
    private Catalog updateRestCatalog(
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

    @GetMapping("/catalog/view-all-by-name")
    @ResponseBody public ResponseEntity<List<ReadCatalogResponseDTO>> retrieveAllCatalogByName(@RequestParam(name = "query", required = false)String namaProduk, HttpServletResponse response) throws SQLException, IOException{
    if (namaProduk != null) {
        List<ReadCatalogResponseDTO> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk);
        return ResponseEntity.status(HttpStatus.OK)
                .body(listCatalogFindByName);
    }
    List<ReadCatalogResponseDTO> listAllCatalog = catalogRestService.retrieveRestAllReadCatalogResponseDTO();
    return ResponseEntity.status(HttpStatus.OK)
            .body(listAllCatalog);
}

    @GetMapping("/catalog/view-all-by-price")
    @ResponseBody public ResponseEntity<List<ReadCatalogResponseDTO>> retrieveAllCatalogByPrice(@RequestParam(name = "query", required = false)Integer hargaProduk){
    if (hargaProduk != null) {
        List<ReadCatalogResponseDTO> listCatalogFindByPrice = catalogRestService.retrieveRestAllCatalogByCatalogPrice(hargaProduk);
        return ResponseEntity.status(HttpStatus.OK).body(listCatalogFindByPrice);
    }
    List<ReadCatalogResponseDTO> listAllCatalog = catalogRestService.retrieveRestAllReadCatalogResponseDTO();
    return ResponseEntity.status(HttpStatus.OK).body(listAllCatalog);
    }

    @DeleteMapping(value = "/catalog/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        catalogRestService.deleteCatalog(id);
        return "Product has been deleted";
    }

}

