package com.apapedia.catalogue.restservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.repository.CategoryDb;
import com.apapedia.catalogue.service.FileStoreService;
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

    @Autowired
    private CategoryDb categoryDb;

    @Autowired
    private FileStoreService fileStoreService;

    @Override
    public List<Catalog> retrieveRestAllCatalog() {
        return catalogDb.findAll();
    }

    @Override
    public List<CatalogRest> getAllCatalogOrderByProductName() {
        List<CatalogRest> result = new ArrayList<>();

        List<Catalog> getAllCatalog = catalogDb.findAllByIsDeletedFalseOrderByProductNameAsc();

        for (Catalog catalog: getAllCatalog) {
            CatalogRest catalogRest = new CatalogRest();
            catalogRest.setIdCatalog(catalog.getIdCatalog());
            catalogRest.setSeller(catalog.getSeller());
            catalogRest.setPrice(catalog.getPrice());
            catalogRest.setProductName(catalog.getProductName());
            catalogRest.setProductDescription(catalog.getProductDescription());
            catalogRest.setCategoryId(catalog.getCategory().getIdCategory());
            catalogRest.setCategoryName(catalog.getCategory().getCategoryName());
            catalogRest.setStock(catalog.getStock());
            catalogRest.setIsDeleted(catalog.getIsDeleted());
            catalogRest.setImage(catalog.getImage());

            result.add(catalogRest);
        }
        return result;
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

    // TODO PERBAIKAN API CREATE REST CATALOG
    @Override
    public CatalogRest createRestCatalog(CreateCatalogueRequestDTO catalogDTO,
                                         MultipartFile imageFiles) throws Exception {

        Optional<Category> category = categoryDb.findByIdCategory(catalogDTO.getCategoryId());
        if (category.isEmpty()) {
            throw new Exception("category not found");
        }

        Catalog catalog = Catalog.builder()
                .idCatalog(catalogDTO.getIdCatalog())
                .seller(catalogDTO.getSeller())
                .price(catalogDTO.getPrice())
                .productName(catalogDTO.getProductName())
                .productDescription(catalogDTO.getProductDescription())
                .category(category.get())
                .stock(catalogDTO.getStock())
                .isDeleted(false)
                .build();

        try {
            if (imageFiles == null) {
                catalog.setImage(null);
            } else {
                fileStoreService.storeFile(imageFiles);
                catalog.setImage(Base64.getEncoder().encodeToString(imageFiles.getBytes()));
            }

            Catalog response = catalogDb.save(catalog);
            return CatalogRest.builder()
                    .idCatalog(response.getIdCatalog())
                    .seller(response.getSeller())
                    .price(response.getPrice())
                    .productName(response.getProductName())
                    .productDescription(response.getProductDescription())
                    .categoryId(response.getCategory().getIdCategory())
                    .categoryName(response.getCategory().getCategoryName())
                    .stock(response.getStock())
                    .image(response.getImage())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    // TODO PERBAIKAN UPDATE CATALOG API
    @Override
    public Catalog updateRestCatalog(CreateCatalogueRequestDTO catalog, MultipartFile imageFiles) throws Exception {

        Optional<Catalog> catalogData = catalogDb.findByIdCatalogAndIsDeletedFalse(catalog.getIdCatalog());
        if (catalogData.isEmpty()) {
            throw new Exception("not found catalog");
        }

        Optional<Category> category = categoryDb.findByIdCategory(catalog.getCategoryId());
        if (category.isEmpty()) {
            throw new Exception("not found catalog");
        }

        catalogData.get().setSeller(catalog.getSeller());
        catalogData.get().setPrice(catalog.getPrice());
        catalogData.get().setProductName(catalog.getProductName());
        catalogData.get().setProductDescription(catalog.getProductDescription());
        catalogData.get().setCategory(category.get());
        catalogData.get().setStock(catalog.getStock());
        catalogData.get().setImage(catalog.getImage() != null ? catalog.getImage() : catalogData.get().getImage());

        return catalogData.get();
    }

    @Override
    public List<CatalogRest> getListCatalogBySellerId(String sellerId) {
        List<CatalogRest> catalogRestList = new ArrayList<>();
        List<Catalog> catalogList = catalogDb.findBySellerAndIsDeletedFalse(UUID.fromString(sellerId));
        for (Catalog catalog : catalogList) {
            CatalogRest catalogRest = CatalogRest.builder()
                    .idCatalog(catalog.getIdCatalog())
                    .seller(catalog.getSeller())
                    .price(catalog.getPrice())
                    .productName(catalog.getProductName())
                    .productDescription(catalog.getProductDescription())
                    .categoryId(catalog.getCategory().getIdCategory())
                    .categoryName(catalog.getCategory().getCategoryName())
                    .stock(catalog.getStock())
                    .image(catalog.getImage())
                    .build();
            catalogRestList.add(catalogRest);
        }
        return catalogRestList;
    }
//    @Override
//    public Catalog createRestCatalog(CreateCatalogueRequestDTO catalogDTO,
//                                         MultipartFile imageFiles) {
//        Catalog catalogRest = new Catalog();
//        var DTO = catalogDTO;
//        try {
//            if (imageFiles == null) {
//                catalogRest.setImage(null);
//            } else {
//                fileStoreService.store(imageFiles);
//                catalogRest.setImage(Base64.getEncoder().encodeToString(imageFiles.getBytes()));
//            }
//            catalogRest.setIdCatalog(catalogDTO.getIdCatalog());
//            catalogRest.setSeller(catalogDTO.getSeller());
//            catalogRest.setPrice(catalogDTO.getPrice());
//            catalogRest.setProductName(catalogDTO.getProductName());
//            catalogRest.setProductDescription(catalogDTO.getProductDescription());
//            // add set categoryId and categoryName here
//
//            // add stock increment here
//
//            catalogRest.setStock(catalogDTO.getStock());
//            catalogRest.setIsDeleted(catalogDTO.getIsDeleted());
//            return catalogDb.save(catalogRest);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    @Override
    public CatalogRest getRestCatalogById(String idCatalog){
    //        for (Catalog catalog: retrieveRestAllCatalog()) {
    //            if (catalog.getIdCatalog().equals(idCatalog)){
    //                return catalog;
    //            }
    //        }
    //        return null;
        Optional<Catalog> catalog = catalogDb.findById(UUID.fromString(idCatalog));
        if (catalog.isEmpty()) {
            return new CatalogRest();
        }
        return CatalogRest.builder()
                .idCatalog(catalog.get().getIdCatalog())
                .seller(catalog.get().getSeller())
                .price(catalog.get().getPrice())
                .productName(catalog.get().getProductName())
                .productDescription(catalog.get().getProductDescription())
                .categoryId(catalog.get().getCategory().getIdCategory())
                .categoryName(catalog.get().getCategory().getCategoryName())
                .stock(catalog.get().getStock())
                .image(catalog.get().getImage())
                .build();
    }

    @Override
    public void saveCatalog(Catalog catalog) {
        catalogDb.save(catalog);
    }

    private byte[] concatenateImages(List<byte[]> images) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] image : images) {
            outputStream.write(image);
        }
        return outputStream.toByteArray();
    }
}
