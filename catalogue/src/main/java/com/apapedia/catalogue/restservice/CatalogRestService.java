package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;

public interface CatalogRestService {
    List<Catalog> retrieveRestAllCatalog();
    List<CatalogRest> retrieveRestAllReadCatalogResponseDTO(); 
    void deleteCatalog(UUID idCatalog);
    List<CatalogRest> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer minPrice, Integer maxPrice);
    void saveCatalog(Catalog catalog); 
}
