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
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.rest.CatalogRest;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogRestService catalogRestService;

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/catalog/all")
    public List<CatalogRest> retrieveAllCatalogue(HttpServletRequest httpServletRequest) {
        return catalogRestService.getAllCatalogOrderByProductName();
    }

    @GetMapping(value = "/catalog/{catalogId}")
    public CatalogRest getCatalogById(@PathVariable(name = "catalogId") String catalogId,
            HttpServletRequest httpServletRequest) {
        return catalogRestService.getCatalogById(catalogId);
    }

    @PostMapping(value = { "/catalog/create" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public CatalogRest createRestCatalogue(@RequestParam("model") String jsonObject,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpServletRequest httpServletRequest) throws Exception {

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw e; 
        }

        return catalogRestService.createRestCatalog(createCatalogueRequestDTO, imageFile);
    }

    @PutMapping(value = { "/catalog/update/{catalogId}" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public CatalogRest editCatalog(@PathVariable(name = "catalogId") String catalogId,
            @RequestParam("model") String jsonObject,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpServletRequest httpServletRequest) throws Exception {

        CreateCatalogueRequestDTO createCatalogueRequestDTO = null;
        try {
            createCatalogueRequestDTO = objectMapper.readValue(jsonObject, CreateCatalogueRequestDTO.class);
            createCatalogueRequestDTO.setIdCatalog(UUID.fromString(catalogId));
        } catch (JsonProcessingException e) {
            throw e;
        }

        return catalogRestService.editRestCatalog(createCatalogueRequestDTO, imageFile);
    }

    @GetMapping(value = { "/catalog/seller/{sellerId}" })
    public List<CatalogRest> getListCatalogBySellerId(@PathVariable(name = "sellerId") String sellerId,
            HttpServletRequest httpServletRequest) {
        return catalogRestService.getListCatalogBySellerId(sellerId);
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ByteArrayResource downloadImage(@PathVariable String id) {
        CatalogRest catalogRest = catalogRestService.getCatalogById(id);
        byte[] image = Base64.getDecoder().decode(catalogRest.getImage());

        return new ByteArrayResource(image);
    }

    String baseUrlUser = "http://localhost:8081";

    @DeleteMapping(value = "/catalog/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        catalogRestService.deleteCatalog(id);
        return "Product has been deleted";
    }

    @GetMapping("/catalog/view-all-by-name")
    @ResponseBody
    public List<CatalogRest> retrieveAllCatalogByName(
            @RequestParam(name = "name", required = false) String namaProduk,
            HttpServletResponse response,
            HttpServletRequest request) {
        if (!namaProduk.isEmpty()) {
            return catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk, null);
        }
        return new ArrayList<>();
    }

    @GetMapping("/catalog/seller-view-all-by-name")
    @ResponseBody
    public List<CatalogRest> sellerViewRetrieveAllCatalogByName(
            @RequestParam(name = "name", required = false) String namaProduk,
            HttpServletResponse response,
            HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class);

        if (userLoggedIn.getStatusCode().is2xxSuccessful()) {
            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString());
            if (!namaProduk.isEmpty()) {
                return catalogRestService.retrieveRestAllCatalogByCatalogName(namaProduk, seller);
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("/catalog/view-all-by-price")
    @ResponseBody
    public List<CatalogRest> retrieveAllCatalogByPrice(
            @RequestParam Integer minPrice, @RequestParam Integer maxPrice) {
        if (minPrice.toString().length() > 0 && maxPrice.toString().length() > 0) {
            return catalogRestService.retrieveRestAllCatalogByCatalogPrice(minPrice, maxPrice, null);

        }
        return new ArrayList<>();
    }

    @GetMapping("/catalog/seller-view-all-by-price")
    @ResponseBody
    public List<CatalogRest> sellerViewRetrieveAllCatalogByPrice(
            @RequestParam Integer minPrice, @RequestParam Integer maxPrice) {

        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class);

        if (userLoggedIn.getStatusCode().is2xxSuccessful()) { // User login
            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString());

            if (minPrice.toString().length() > 0 && maxPrice.toString().length() > 0) {
                return catalogRestService
                        .retrieveRestAllCatalogByCatalogPrice(minPrice, maxPrice, seller);
            }
        }
       return new ArrayList<>();
    }

    @GetMapping("/catalog/view-all-sort-by")
    @ResponseBody
    public List<CatalogRest> retrieveAllSortBy(@RequestParam(defaultValue = "productName") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        return catalogRestService.findAllSortBy(sortDirection, sortField, null);
    }

    @GetMapping("/catalog/seller-view-all-sort-by")
    @ResponseBody
    public List<CatalogRest> sellerViewRetrieveAllSortBy(@RequestParam(defaultValue = "productName") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(url, Object.class);

        if (userLoggedIn.getStatusCode().is2xxSuccessful()) { // User login

            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString());

            return catalogRestService.findAllSortBy(sortDirection, sortField, seller);
        }
       return new ArrayList<>();
    }
}