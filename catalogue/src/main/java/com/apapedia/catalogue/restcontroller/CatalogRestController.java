package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Dictionary;

import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.rest.CatalogRest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogRestService catalogRestService;

    @Autowired
    private CatalogMapper catalogMapper;

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

    @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Catalog createRestCatalogue(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogDTO,
                                       BindingResult bindingResult,
                                       @RequestParam("image") MultipartFile[] imageFiles)
            throws IOException {
        // Upload images and create a catalog
        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
        catalogRestService.createRestCatalog(catalog, imageFiles);
        return catalog;

    }
}


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
//    @PutMapping(value = "/catalog/{idCatalog}")
//    private Catalog updateRestCatalog(
//            @PathVariable("idCatalog") UUID idCatalog,
//            @RequestBody UpdateCatalogRequestDTO updateCatalogRequestDTO) {
//        try {
//            return catalogRestService.updateCatalog(idCatalog, updateCatalogRequestDTO);
//
//        } catch (NoSuchElementException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Catalog No" + idCatalog + " Not Found!"
//            );
//
//        } catch (UnsupportedOperationException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, " "
//            );
//        }
//    }

