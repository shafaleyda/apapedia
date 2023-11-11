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
        List<Catalog> result = new ArrayList<>(); 

        for (Catalog cat: catalogDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(catalogName)) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                result.add(Catalog.builder()
                    .price(catalog.get().getPrice())
                    .productName(catalog.get().getProductName())
                    .productDescription(catalog.get().getProductDescription())
                    .category(catalog.get().getCategory())
                    .stock(catalog.get().getStock())
                    .isDeleted(catalog.get().getIsDeleted())
                    .image(decompressImage(catalog.get().getImage())).build());
            }
        }
        
        return result;
    }

    @Override
    public List<Catalog> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice) {
        return catalogDb.findByPriceOrderByPriceAsc(catalogPrice); 
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
