package com.apapedia.catalogue.service;

import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;

import java.util.List;

@Service
public interface CatalogService {
    void saveCatalog(Catalog catalog);
    List<CatalogRest> getAllCatalog(); 
}
