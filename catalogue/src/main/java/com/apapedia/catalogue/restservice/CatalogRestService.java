package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;

public interface CatalogRestService {
    List<Catalog> retrieveRestAllCatalog();
    List<ReadCatalogResponseDTO> retrieveRestAllReadCatalogResponseDTO(); 
    void deleteCatalog(UUID idCatalog);
    List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice);
    byte[] decompressImage(byte[] data); 
    void saveCatalog(Catalog catalog); 
}
