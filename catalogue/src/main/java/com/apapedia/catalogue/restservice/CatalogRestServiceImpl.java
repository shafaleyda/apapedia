package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CatalogRestServiceImpl implements CatalogRestService{
    @Autowired
    private CatalogDb catalogDb;

    @Override
    public List<Catalog> retrieveRestAllCatalog() {
        return catalogDb.findAll(Sort.by(Sort.Direction.ASC, "productName"));

    }

    @Override
    public void deleteCatalog(UUID idCatalog) {
        for (Catalog catalog: retrieveRestAllCatalog()) {
            if (catalog.getIdCatalog().equals(idCatalog)) {
                catalogDb.delete(catalog);
            }
        }
    }

//    @Override
//    public List<Catalog> retrieveRestAllCatalogByCatalogName(String catalogName) {
//        return catalogDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(catalogName);
//    }
//
//    @Override
//    public List<Catalog> retrieveRestAllCatalogByCatalogPrice(int catalogPrice) {
//        return catalogDb.findByOrderByProductPriceAsc(catalogPrice);
//    }

    @Override
    public Catalog createRestCatalog(Catalog catalog) {
        return catalogDb.save(catalog);
    };

    @Override
    public Catalog updateCatalog (
            UUID idCatalog,
            UpdateCatalogRequestDTO updateCatalogRequestDTO) {
        Catalog catalog = getRestCatalogById(idCatalog);
        Category category = catalog.getCategoryId();

        var catalogDTO = updateCatalogRequestDTO;

        catalog.setProductName(catalogDTO.getProductName());
        catalog.setProductPrice(catalogDTO.getProductPrice());
        catalog.setProductDescription(catalogDTO.getProductDescription());
        catalog.setStock(catalogDTO.getStock());
        catalog.setImage(catalogDTO.getImage());
        catalog.setCategoryId(catalogDTO.getCategoryName());
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

}
