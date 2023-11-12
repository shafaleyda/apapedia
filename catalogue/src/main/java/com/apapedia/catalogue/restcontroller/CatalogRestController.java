package com.apapedia.catalogue.restcontroller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
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

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogRestService catalogRestService; 
    
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
