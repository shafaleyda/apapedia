package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.model.Catalog;

public interface CatalogRestService {
    List<Catalog> retrieveRestAllCatalog();
    void deleteCatalog(UUID idCatalog);
    List<Catalog> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<Catalog> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice);
    //Catalog getImage(UUID idCatalog);
    byte[] decompressImage(byte[] data); 
    void saveCatalog(Catalog catalog); 
}
