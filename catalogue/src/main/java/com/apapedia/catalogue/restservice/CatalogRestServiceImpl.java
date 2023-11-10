package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
        return catalogDb.findAll(); 

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
    public List<Catalog> retrieveRestAllCatalogByCatalogName(String catalogName) {
        return catalogDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(catalogName);
    }

    @Override
    public List<Catalog> retrieveRestAllCatalogByCatalogPrice(int catalogPrice) {
        return catalogDb.findByOrderByProductPriceAsc(catalogPrice); 
    }
    
}
