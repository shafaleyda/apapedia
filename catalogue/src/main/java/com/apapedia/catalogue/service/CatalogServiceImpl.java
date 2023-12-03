package com.apapedia.catalogue.service;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.repository.CatalogDb;
import com.apapedia.catalogue.rest.CatalogRest;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    CatalogDb catalogDb;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void saveCatalog(Catalog catalog)    {
        catalogDb.save(catalog);
    }

    @Override
    public CatalogRest createCatalog(CreateCatalogueRequestDTO createRequest, MultipartFile imageFile) throws Exception {

        // buat configuration agar tidak terlalu sering instansiasi object dan duplication code
        // https://www.geeksforgeeks.org/spring-boot-configure-a-resttemplate-with-resttemplatebuilder/
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Set up request data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Ubah multipart Menjadi Byte
        ByteArrayResource contentsAsResource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        body.add("model",objectMapper.writeValueAsString(createRequest));
        // untuk multivalue input
        body.add("image", contentsAsResource);


        // Create an HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the HTTP request
        ResponseEntity<CatalogRest> responseEntity = restTemplate.exchange(
                "http://localhost:8989/api/catalog/create",
                HttpMethod.POST,
                requestEntity,
                CatalogRest.class);

        // Return the response body
        return responseEntity.getBody();
    }

    @Override
    public List<CatalogRest> createRestCatalog(CatalogRest catalogRest, MultipartFile imageFile) throws Exception {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Set up request data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource contentsAsResource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        body.add("model",objectMapper.writeValueAsString(catalogRest));
        body.add("image", contentsAsResource);


        // Create an HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the POST request
        restTemplate.postForEntity(
                "http://localhost:8989/api/catalog/create",
                requestEntity,
                CatalogRest.class
        );

//        List<CreateCatalogueRequestDTO> result = new ArrayList<>();
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            CatalogRest catalogsArray[] = responseEntity.getBody();
//            if (catalogsArray != null) {
//
//                return Arrays.asList(catalogsArray); // Convert array to list
//            }
//        }
        return null;
    }

    @Override
    public List<CatalogRest> updateRestCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDto, MultipartFile imageFile) throws Exception {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Set up request data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Create an HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the POST request
        ResponseEntity<CatalogRest[]> responseEntity = restTemplate.postForEntity(
                "http://localhost:8989/api/catalog/update/{idCatalog}",
                requestEntity,
                CatalogRest[].class
        );

        List<CreateCatalogueRequestDTO> result = new ArrayList<>();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            CatalogRest catalogsArray[] = responseEntity.getBody();
            if (catalogsArray != null) {

                return Arrays.asList(catalogsArray); // Convert array to list
            }
        }
        return null;
    }

    @Override
    public CatalogRest updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDto, MultipartFile imageFile) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Set up request data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource contentsAsResource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        body.add("model",objectMapper.writeValueAsString(updateCatalogRequestDto));
        body.add("image", contentsAsResource);


        // Create an HttpEntity with headers and body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the HTTP request
        ResponseEntity<CatalogRest> responseEntity = restTemplate.exchange(
                "http://localhost:8989/api/catalog/update/"+id.toString(),
                HttpMethod.PUT,
                requestEntity,
                CatalogRest.class);

        // Return the response body
        return responseEntity.getBody();
    }

//    @Override
//    public void updateBarang(String id, UpdateBarangRequestDTO updateBarangRequestDto) {
//        var barangDto = this.getBarangById(id).orElseThrow(() -> new NoSuchElementException("Barang tidak ditemukan"));
//
//        Barang barang = barangMapper.barangDtoToBarang(barangDto);
//        barang.setSku(id);
//        barang.setMerk(updateBarangRequestDto.getMerk());
//        barang.setHargaBarang(updateBarangRequestDto.getHargaBarang());
//
//        barangDb.save(barang);
//    }

    @Override
    public List<CatalogRest> getAllCatalog() {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        // Create an HttpEntity with the custom headers
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        
        ResponseEntity<CatalogRest[]> response = restTemplate.getForEntity(
                "http://localhost:8080/api/catalog/all",
                CatalogRest[].class
        );

        List<ReadCatalogResponseDTO> result = new ArrayList<>(); 
        if (response.getStatusCode().is2xxSuccessful()) {
            CatalogRest[] catalogsArray = response.getBody();
            if (catalogsArray != null) {

                return Arrays.asList(catalogsArray); // Convert array to list
            }
        }
        return null;
    }

}

