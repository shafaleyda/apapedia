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

import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;

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
            List<ReadCatalogResponseDTO> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogPrice(hargaProduk);
            return ResponseEntity.status(HttpStatus.OK)
            .body(listCatalogFindByName);
        } 
        List<ReadCatalogResponseDTO> listAllCatalog = catalogRestService.retrieveRestAllReadCatalogResponseDTO();
            return ResponseEntity.status(HttpStatus.OK)
            .body(listAllCatalog); 
    } 
}
