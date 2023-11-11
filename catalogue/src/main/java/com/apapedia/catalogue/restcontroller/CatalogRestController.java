package com.apapedia.catalogue.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.Blob;


import com.apapedia.catalogue.restservice.CatalogRestService;

import jakarta.servlet.http.HttpServletResponse;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogRestService catalogRestService; 
    
    @Autowired
    private CatalogDb catalogDb; 

    @GetMapping("catalog/{id}/delete")
    public String deleteProduct(@PathVariable UUID id) {
        catalogRestService.deleteCatalog(id);
        return "Product has been deleted"; 
    }

    @GetMapping("/catalog/view-all-by-name")
    @ResponseBody public ResponseEntity<List<Catalog>> retrieveAllCatalogByName(@RequestParam(name = "query", required = false)String namaProduk, HttpServletResponse response) throws SQLException, IOException{
        if (namaProduk != null) {
            List<Catalog> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk);
            return ResponseEntity.status(HttpStatus.OK)
            .body(listCatalogFindByName);
        } 
        List<Catalog> listAllCatalog = catalogRestService.retrieveRestAllCatalog();
            return ResponseEntity.status(HttpStatus.OK)
            .body(listAllCatalog); 
    } //cek lagi

    @GetMapping("/catalog/view-all-by-price")
    @ResponseBody public List<Catalog> retrieveAllCatalogByPrice(@RequestParam(name = "query", required = false)Integer hargaProduk){
        if (hargaProduk != null) {
            return catalogRestService.retrieveRestAllCatalogByCatalogPrice(hargaProduk); 
        } return catalogRestService.retrieveRestAllCatalogByCatalogPrice(hargaProduk); 
    } //cek lagi
}
