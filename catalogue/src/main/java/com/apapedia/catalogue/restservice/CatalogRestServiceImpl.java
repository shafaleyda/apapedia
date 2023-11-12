package com.apapedia.catalogue.restservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.Inflater;
import java.util.ArrayList;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;
import com.apapedia.catalogue.rest.CatalogRest;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CatalogRestServiceImpl implements CatalogRestService{
    @Autowired
    private CatalogDb catalogDb;

    @Override
    public List<Catalog> retrieveRestAllCatalog() {
        return catalogDb.findAll();
    }

    @Override
    public List<CatalogRest> retrieveRestAllReadCatalogResponseDTO() {
        List<CatalogRest> result = new ArrayList<>();

        for (Catalog cat: retrieveRestAllCatalog()) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                CatalogRest catalogRest = new CatalogRest();
                catalogRest.setIdCatalog(catalog.get().getIdCatalog());
                catalogRest.setSeller(catalog.get().getSeller());
                catalogRest.setPrice(catalog.get().getPrice());
                catalogRest.setProductName(catalog.get().getProductName());
                catalogRest.setProductDescription(catalog.get().getProductDescription());
                catalogRest.setCategoryId(catalog.get().getCategory().getIdCategory());
                catalogRest.setCategoryName(catalog.get().getCategory().getCategoryName());
                catalogRest.setStock(catalog.get().getStock());
                catalogRest.setIsDeleted(catalog.get().getIsDeleted());
                catalogRest.setImage(catalog.get().getImage());

                result.add(catalogRest);
            }
        }
        return result;
    }

    @Override
    public void deleteCatalog(UUID idCatalog) {
        for (Catalog catalog: retrieveRestAllCatalog()) {
            if (catalog.getIdCatalog().equals(idCatalog)) {
                catalogDb.delete(catalog);
            }
        }
    }

    @Override
    public List<CatalogRest> retrieveRestAllCatalogByCatalogName(String catalogName) {
        List<CatalogRest> result = new ArrayList<>();

        for (Catalog cat: catalogDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(catalogName)) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                CatalogRest catalogRest = new CatalogRest();
                catalogRest.setIdCatalog(catalog.get().getIdCatalog());
                catalogRest.setSeller(catalog.get().getSeller());
                catalogRest.setPrice(catalog.get().getPrice());
                catalogRest.setProductName(catalog.get().getProductName());
                catalogRest.setProductDescription(catalog.get().getProductDescription());
                catalogRest.setCategoryId(catalog.get().getCategory().getIdCategory());
                catalogRest.setCategoryName(catalog.get().getCategory().getCategoryName());
                catalogRest.setStock(catalog.get().getStock());
                catalogRest.setIsDeleted(catalog.get().getIsDeleted());
                catalogRest.setImage(catalog.get().getImage());
                result.add(catalogRest);
            }
        }

        return result;
    }

    @Override
    public List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer minPrice, Integer maxPrice) {
        List<CatalogRest> result = new ArrayList<>();

        for (Catalog cat: catalogDb.findByPriceBetween(minPrice, maxPrice)) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                CatalogRest catalogRest = new CatalogRest();
                catalogRest.setIdCatalog(catalog.get().getIdCatalog());
                catalogRest.setSeller(catalog.get().getSeller());
                catalogRest.setPrice(catalog.get().getPrice());
                catalogRest.setProductName(catalog.get().getProductName());
                catalogRest.setProductDescription(catalog.get().getProductDescription());
                catalogRest.setCategoryId(catalog.get().getCategory().getIdCategory());
                catalogRest.setCategoryName(catalog.get().getCategory().getCategoryName());
                catalogRest.setStock(catalog.get().getStock());
                catalogRest.setIsDeleted(catalog.get().getIsDeleted());
                catalogRest.setImage(catalog.get().getImage());
                result.add(catalogRest);
            }
        }

        return result;
    }

    @Override
    public void saveCatalog(Catalog catalog) {
        catalogDb.save(catalog);
    }

    @Override
    public void createRestCatalog(Catalog catalog, MultipartFile[] imageFiles) throws IOException {
        catalogDb.save(catalog);
    };

    private byte[] concatenateImages(List<byte[]> images) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] image : images) {
            outputStream.write(image);
        }
        return outputStream.toByteArray();
    }

    @Override
    public Catalog updateRestCatalog (
            UUID idCatalog,
            UpdateCatalogRequestDTO updateCatalogRequestDTO) {
        Catalog catalog = getRestCatalogById(idCatalog);
        Category category = catalog.getCategory();

        var catalogDTO = updateCatalogRequestDTO;

        catalog.setProductName(catalogDTO.getProductName());
        catalog.setPrice(catalogDTO.getPrice());
        catalog.setProductDescription(catalogDTO.getProductDescription());
        catalog.setStock(catalogDTO.getStock());
        catalog.setImage(catalogDTO.getImage());
        return catalogDb.save(catalog);
    }

    @Override
    public Catalog getRestCatalogById(UUID idCatalog){
        for (Catalog catalog: retrieveRestAllCatalog()) {
            if (catalog.getIdCatalog().equals(idCatalog)){
                return catalog;
            }
        }
        return null;
    }


//    @Override
//    public byte[] decompressImage(byte[] data) {
//        return new byte[0];
//    }


}
