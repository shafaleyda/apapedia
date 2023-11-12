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
    List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice);
    byte[] decompressImage(byte[] data); 
    void saveCatalog(Catalog catalog); 
}
