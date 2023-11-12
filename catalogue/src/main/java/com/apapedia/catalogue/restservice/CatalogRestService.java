package com.apapedia.catalogue.restservice;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;
import org.springframework.web.multipart.MultipartFile;

public interface CatalogRestService {

    List<Catalog> retrieveRestAllCatalog();
    List<CatalogRest> retrieveRestAllReadCatalogResponseDTO();
    void deleteCatalog(UUID idCatalog);
    List<CatalogRest> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer minPrice, Integer maxPrice);
    void saveCatalog(Catalog catalog);

//    byte[] decompressImage(byte[] data);

    void createRestCatalog(Catalog catalog, MultipartFile[] imageFiles) throws IOException;
    Catalog updateRestCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO);
    Catalog getRestCatalogById(UUID idCatalog);

}
