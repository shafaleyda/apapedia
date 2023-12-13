package com.apapedia.catalogue.service;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.rest.CatalogRest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface CatalogService {
    void saveCatalog(Catalog catalog);
    List<CatalogRest> getAllCatalog();
    CatalogRest createCatalog(CreateCatalogueRequestDTO createRequest, MultipartFile imageFile) throws Exception;
    List<CatalogRest> createRestCatalog(CatalogRest catalogRest, MultipartFile imageFile) throws Exception;
    List<CatalogRest> updateRestCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDto, MultipartFile imageFile) throws Exception;
    CatalogRest updateCatalog(UUID id, UpdateCatalogRequestDTO updateCatalogRequestDto, MultipartFile imageFile) throws Exception;

}
