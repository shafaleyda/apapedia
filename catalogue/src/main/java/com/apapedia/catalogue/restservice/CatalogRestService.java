package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;

public interface CatalogRestService {
    List<Catalog> retrieveRestAllCatalog();
    void deleteCatalog(UUID idCatalog);
//    List<Catalog> retrieveRestAllCatalogByCatalogName(String catalogName);
//    List<Catalog> retrieveRestAllCatalogByCatalogPrice(int catalogPrice);

    Catalog getRestCatalogById(UUID idCatalog);
    Catalog createRestCatalog(Catalog catalog);
    Catalog updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO);
}
