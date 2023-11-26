package com.apapedia.catalogue.restservice;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

@Service
@Transactional
public interface CatalogRestService {

    List<Catalog> retrieveRestAllCatalog();
    List<CatalogRest> getAllCatalogOrderByProductName();
    CatalogRest getCatalogById(String id);
    List<CatalogRest> retrieveRestAllReadCatalogResponseDTO();
    void deleteCatalog(UUID idCatalog);
    List<CatalogRest> retrieveRestAllCatalogByCatalogName(String catalogName);
    List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer minPrice, Integer maxPrice);

    //    CatalogRest createRestCatalog(CreateCatalogueRequestDTO catalog, MultipartFile imageFiles) throws IOException;
    CatalogRest createRestCatalog(CreateCatalogueRequestDTO catalog, MultipartFile imageFiles) throws Exception;
    CatalogRest editRestCatalog(CreateCatalogueRequestDTO catalog, MultipartFile imageFiles) throws Exception;
    Catalog updateRestCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDTO);
    Catalog getRestCatalogById(UUID idCatalog);
    List<CatalogRest> getListCatalogBySellerId(String sellerId);
    void saveCatalog(Catalog catalog);
    List<CatalogRest> findAllSortBy(Sort sort);
}
