package com.apapedia.catalogue.restcontroller;

//import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
//import com.apapedia.catalogue.service.FileStorageService;
//import com.apapedia.catalogue.service.StorageService;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.service.FileStoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Paths;
import java.util.*;
import java.sql.SQLException;
import java.io.IOException;

import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.rest.CatalogRest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private CatalogRestService catalogRestService;


    @Autowired
    private ObjectMapper objectMapper;

    // TODO GET ALL CATALOG WITH SELLER ROLE
    @GetMapping(value="/catalog/all")
    private List<CatalogRest> retrieveAllCatalogue(){
        return catalogRestService.getAllCatalogOrderByProductName();

    }

    // CREATE CATALOG API WITH CUSTOMER AND SELLER ROLE
    @PostMapping(value = { "/catalog/create" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CatalogRest createRestCatalogue(@RequestParam("model") String jsonObject,
                                           @RequestParam(value = "image", required = false) MultipartFile imageFile) throws Exception {

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return catalogRestService.createRestCatalog(createCatalogueRequestDTO, imageFile);
    }

    // TODO UPDATE CATALOG API WITH ROLE SELLER
    @PutMapping(value = { "/catalog/update/{catalogId}" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Catalog editCatalog(@PathVariable(name = "catalogId") String catalogId,
                               @RequestParam("model") String jsonObject,
                               @RequestParam(value = "image", required = false) MultipartFile imageFile) throws Exception {

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
            createCatalogueRequestDTO.setIdCatalog(UUID.fromString(catalogId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return catalogRestService.updateRestCatalog(createCatalogueRequestDTO, imageFile);
    }

    // TODO GET CATALOG BY SELLER ID WITH CUSTOMER AND SELLER ROLE
    @GetMapping(value = {"/catalog/seller/{sellerId}"})
    public List<CatalogRest> getListCatalogBySellerId(@PathVariable(name = "sellerId") String sellerId) {
        return catalogRestService.getListCatalogBySellerId(sellerId);
    }

    // TODO GET CATALOG BY CATALOG ID WITH CUSTOMER AND SELLER ROLE
    @GetMapping(value = "/catalog/{catalogId}")
    public CatalogRest getCatalogById(@PathVariable(name = "catalogId") String idCatalog) {
        return catalogRestService.getRestCatalogById(idCatalog);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------

    @DeleteMapping(value = "/catalog/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        catalogRestService.deleteCatalog(id);
        return "Product has been deleted";
    }

    @GetMapping("/catalog/view-all-by-name")
    @ResponseBody public ResponseEntity<Dictionary<String, Object>> retrieveAllCatalogByName(@RequestParam(name = "query", required = false)String namaProduk, HttpServletResponse response) throws SQLException, IOException{
        Dictionary<String, Object> responseData= new Hashtable<>();
        if (namaProduk.length() > 0){
            List<CatalogRest> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk);
            responseData.put("status", HttpStatus.OK.value());
            responseData.put("data", listCatalogFindByName);
            responseData.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseData);
        }
        List<CatalogRest> listAllCatalog = catalogRestService.retrieveRestAllReadCatalogResponseDTO();
        responseData.put("status", HttpStatus.OK.value());
        responseData.put("data", listAllCatalog);
        responseData.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }

    @GetMapping("/catalog/view-all-by-price")
    @ResponseBody public ResponseEntity<Dictionary<String, Object>> retrieveAllCatalogByPrice(@RequestParam Integer minPrice, @RequestParam Integer maxPrice){
        Dictionary<String, Object> responseData= new Hashtable<>();
        if (minPrice.toString().length() > 0 && maxPrice.toString().length() > 0) {
            List<CatalogRest> listCatalogFindByPrice = catalogRestService.retrieveRestAllCatalogByCatalogPrice(minPrice, maxPrice);
            responseData.put("status", HttpStatus.OK.value());
            responseData.put("data", listCatalogFindByPrice);
            responseData.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseData);
        }
        List<CatalogRest> listAllCatalog = catalogRestService.retrieveRestAllReadCatalogResponseDTO();
        responseData.put("status", HttpStatus.OK.value());
        responseData.put("data", listAllCatalog);
        responseData.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }

}












//      @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public Catalog createRestCatalogue(@Valid @RequestBody CreateCatalogueRequestDTO catalogDTO,
//                                       BindingResult bindingResult,
//                                       @RequestParam("image") MultipartFile[] imageFiles,
//                                       @RequestParam("model") String jsonObject)
//            throws IOException {
//
//        // Upload images and create a catalog
//        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
//        // Handle file uploads
//        List<String> fileNames = Arrays.stream(imageFiles)
//                .map(file -> {
//                    return fileStorageService.storeFile(file);
//                })
//                .collect(Collectors.toList());
//        String concatenatedFileNames = String.join(",", fileNames);
//        byte[] fileNamesBytes = concatenatedFileNames.getBytes(StandardCharsets.UTF_8);
//
//        return catalog;
//
//    }

//    @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public Catalog createRestCatalogue(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogDTO,
//                                       BindingResult bindingResult,
//                                       @RequestParam("image") MultipartFile[] imageFiles)
//            throws IOException {
//
//        // Upload images and create a catalog
//        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
//        catalogRestService.createRestCatalog(catalog, imageFiles);
//        return catalog;
//
//    }
//

