package com.apapedia.catalogue.restservice;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.ArrayList; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;
import com.apapedia.catalogue.rest.CatalogRest;

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
    public List<CatalogRest> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice) {
        List<CatalogRest> result = new ArrayList<>(); 
        
        for (Catalog cat: catalogDb.findByPriceOrderByPriceAsc(catalogPrice)) {
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
    public byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
        }
        return outputStream.toByteArray();
    }

    @Override
    public void saveCatalog(Catalog catalog) { 
        catalogDb.save(catalog); 
    }
    
}
