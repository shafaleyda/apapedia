package com.apapedia.catalogue.restservice;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Catalog;
import org.springframework.web.multipart.MultipartFile;

public interface CatalogRestService {
    List<Catalog> retrieveRestAllCatalog();
    void deleteCatalog(UUID idCatalog);
    byte[] decompressImage(byte[] data);

    List<ReadCatalogResponseDTO> retrieveRestAllReadCatalogResponseDTO();
    List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice);
    Catalog getRestCatalogById(UUID idCatalog);

    void createRestCatalog(Catalog catalog, MultipartFile[] imageFiles, String jsonObject) throws IOException;
    /*void createRestCatalog(Catalog catalog);*/
    Catalog updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO);
    void saveCatalog(Catalog catalog);

}
