package com.apapedia.catalogue.service;

import com.apapedia.catalogue.repository.CatalogDb;
import com.apapedia.catalogue.rest.CatalogRest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;
import java.util.Arrays;
import java.util.ArrayList; 

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    CatalogDb catalogDb;

    @Override
    public void saveCatalog(Catalog catalog)    {
        catalogDb.save(catalog);
    }

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
