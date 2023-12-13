package com.apapedia.catalogue.restcontroller;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import java.sql.SQLException;
import java.io.IOException;

import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.rest.CatalogRest;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogRestService catalogRestService; 

    @Autowired
    private ObjectMapper objectMapper;

    // TODO GET ALL CATALOG WITH CUSTOMER ROLE
    @GetMapping(value="/catalog/all")
    private List<CatalogRest> retrieveAllCatalogue(HttpServletRequest httpServletRequest){
        return catalogRestService.getAllCatalogOrderByProductName();
    }

    // TODO GET CATALOG BY CATALOG ID WITH CUSTOMER AND SELLER ROLE
    @GetMapping(value = "/catalog/{catalogId}")
    public CatalogRest getCatalogById(@PathVariable(name = "catalogId") String catalogId,
                                      HttpServletRequest httpServletRequest) {
        return catalogRestService.getCatalogById(catalogId);
    }

    // TODO CREATE CATALOG API WITH SELLER ROLE
    @PostMapping(value = { "/catalog/create" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CatalogRest createRestCatalogue(@RequestParam("model") String jsonObject,
                                           @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                           HttpServletRequest httpServletRequest) throws Exception {

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return catalogRestService.createRestCatalog(createCatalogueRequestDTO, imageFile);
    }


    // TODO UPDATE CATALOG API WITH SELLER ROLE
    @PutMapping(value = { "/catalog/update/{catalogId}" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CatalogRest editCatalog(@PathVariable(name = "catalogId") String catalogId,
                                   @RequestParam("model") String jsonObject,
                                   @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                   HttpServletRequest httpServletRequest) throws Exception {

        // coba pelajari https://orika-mapper.github.io/orika-docs/ lebih bagus untuk convert convert data
        // https://restfulapi.net/resource-naming/ untuk naming juga bagus nya sesuai standar agar front-end mobile maupun web lebih mudah

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
            createCatalogueRequestDTO.setIdCatalog(UUID.fromString(catalogId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return catalogRestService.editRestCatalog(createCatalogueRequestDTO, imageFile);
    }


    // TODO GET CATALOG BY SELLER ID WITH CUSTOMER AND SELLER ROLE
    @GetMapping(value = {"/catalog/seller/{sellerId}"})
    public List<CatalogRest> getListCatalogBySellerId(@PathVariable(name = "sellerId") String sellerId,
                                                      HttpServletRequest httpServletRequest) {
        return catalogRestService.getListCatalogBySellerId(sellerId);
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    ByteArrayResource downloadImage(@PathVariable String id) {
        CatalogRest catalogRest = catalogRestService.getCatalogById(id);
        byte[] image = Base64.getDecoder().decode(catalogRest.getImage());

        return new ByteArrayResource(image);
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------

    String baseUrlUser = "http://localhost:8081"; 

    //Delete Catalog
    @DeleteMapping(value = "/catalog/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        catalogRestService.deleteCatalog(id);
        return "Product has been deleted"; 
    }

    //View Catalog by Name (Guest)
    @GetMapping("/catalog/view-all-by-name")
    @ResponseBody public List<CatalogRest>  retrieveAllCatalogByName(
            @RequestParam(name = "name", required = false) String namaProduk,
            HttpServletResponse response, 
            HttpServletRequest request) throws SQLException, IOException
    {
        if (!namaProduk.isEmpty()) {
            List<CatalogRest> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk, null);
            return listCatalogFindByName;
        }
        return null;
    }

    //View Catalog by Name (Seller -- Harus Login)
    @GetMapping("/catalog/seller-view-all-by-name")
    @ResponseBody public List<CatalogRest>  sellerViewRetrieveAllCatalogByName(
            @RequestParam(name = "name", required = false) String namaProduk,
            HttpServletResponse response, 
            HttpServletRequest request) throws SQLException, IOException
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class); 

        if(userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString()); 
            if(!namaProduk.isEmpty()) {
                List<CatalogRest> listCatalogFindByName = catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk, seller);
                return listCatalogFindByName;
            }
        }
        return null;
    }

    //View Catalog By Range Price (Guest)
    @GetMapping("/catalog/view-all-by-price")
    @ResponseBody public List<CatalogRest> retrieveAllCatalogByPrice(
            @RequestParam Integer minPrice, @RequestParam Integer maxPrice)
    {
        if (minPrice.toString().length() > 0 && maxPrice.toString().length() > 0) {
            List<CatalogRest> listCatalogFindByPrice = catalogRestService.retrieveRestAllCatalogByCatalogPrice(minPrice, maxPrice, null);
            return listCatalogFindByPrice;
        } return null;
    }

    //View Catalog By Range Price (Seller)
    @GetMapping("/catalog/seller-view-all-by-price")
    @ResponseBody public List<CatalogRest> sellerViewRetrieveAllCatalogByPrice(
            @RequestParam Integer minPrice, @RequestParam Integer maxPrice)
    {

        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class); 

        if(userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString()); 

            if (minPrice.toString().length() > 0 && maxPrice.toString().length() > 0) {
                List<CatalogRest> listCatalogFindByPrice = catalogRestService.retrieveRestAllCatalogByCatalogPrice(minPrice, maxPrice, seller);
                return listCatalogFindByPrice;
            }            
        }
        return null;
    }
    
    //Sort Catalog (Guest)
    @GetMapping("/catalog/view-all-sort-by")
    @ResponseBody public List<CatalogRest> retrieveAllSortBy(@RequestParam(defaultValue = "productName") String sortField,
                                                                                      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) 
    {
        // Sort sort = Sort.by(sortDirection, sortField); 
        List<CatalogRest> listAllCatalogSortBy = catalogRestService.findAllSortBy(sortDirection, sortField, null);
        return listAllCatalogSortBy;
    }

    //Sort Catalog (User Logged In)
    @GetMapping("/catalog/seller-view-all-sort-by")
    @ResponseBody public List<CatalogRest> sellerViewRetrieveAllSortBy(@RequestParam(defaultValue = "productName") String sortField,
                                                                                      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) 
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class); 

        if(userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login

            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString()); 

            List<CatalogRest> listAllCatalogSortBy = catalogRestService.findAllSortBy(sortDirection, sortField, seller);
            return listAllCatalogSortBy;
        }

        return null; 
    }
}