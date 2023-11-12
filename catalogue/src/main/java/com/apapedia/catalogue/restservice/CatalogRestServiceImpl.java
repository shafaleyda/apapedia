package com.apapedia.catalogue.restservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.Inflater;

import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CatalogRestServiceImpl implements CatalogRestService{
    @Autowired
    private CatalogDb catalogDb;

    @Autowired
    private ImageUtils imageUtils;

    @Override
    public List<Catalog> retrieveRestAllCatalog() {
        return catalogDb.findAll(Sort.by(Sort.Direction.ASC, "productName"));
    }

    @Override
    public void createRestCatalog(Catalog catalog, MultipartFile[] imageFiles, String jsonObject) throws IOException {
        // Fetch and compress images
        List<byte[]> compressedImages = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            compressedImages.add(imageUtils.fetchAndCompressImage(file.getOriginalFilename()));
        }

        // Decompress the images
        List<byte[]> decompressedImages = new ArrayList<>();
        for (byte[] compressedImage : compressedImages) {
            decompressedImages.add(decompressImage(compressedImage));
        }

        // Assuming that you want to concatenate the decompressed images
        // You may need to adjust this based on your specific requirements
        byte[] concatenatedImages = concatenateImages(decompressedImages);

        // Set the concatenated images to the catalog
        catalog.setImage(concatenatedImages);

        // Save the catalog with images
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
    public List<ReadCatalogResponseDTO> retrieveRestAllReadCatalogResponseDTO() {
        List<ReadCatalogResponseDTO> result = new ArrayList<>();

        for (Catalog cat: retrieveRestAllCatalog()) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                result.add(ReadCatalogResponseDTO.builder()
                        .idCatalog(catalog.get().getIdCatalog())
                        .seller(catalog.get().getSeller())
                        .price(catalog.get().getPrice())
                        .productName(catalog.get().getProductName())
                        .productDescription(catalog.get().getProductDescription())
                        .categoryId(catalog.get().getCategory().getIdCategory())
                        .categoryName(catalog.get().getCategory().getCategoryName())
                        .stock(catalog.get().getStock())
                        .isDeleted(catalog.get().getIsDeleted())
                        .image(decompressImage(catalog.get().getImage())).build());
            }
        }
        return result;
    }

    @Override
    public List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogName(String catalogName) {
        List<ReadCatalogResponseDTO> result = new ArrayList<>();

        for (Catalog cat: catalogDb.findByProductNameContainingIgnoreCaseOrderByProductNameAsc(catalogName)) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                result.add(ReadCatalogResponseDTO.builder()
                        .idCatalog(catalog.get().getIdCatalog())
                        .seller(catalog.get().getSeller())
                        .price(catalog.get().getPrice())
                        .productName(catalog.get().getProductName())
                        .productDescription(catalog.get().getProductDescription())
                        .categoryId(catalog.get().getCategory().getIdCategory())
                        .categoryName(catalog.get().getCategory().getCategoryName())
                        .stock(catalog.get().getStock())
                        .isDeleted(catalog.get().getIsDeleted())
                        .image(decompressImage(catalog.get().getImage())).build());
            }
        }

        return result;
    }

    @Override
    public List<ReadCatalogResponseDTO> retrieveRestAllCatalogByCatalogPrice(Integer catalogPrice) {
        List<ReadCatalogResponseDTO> result = new ArrayList<>();

        for (Catalog cat: catalogDb.findByPriceOrderByPriceAsc(catalogPrice)) {
            Optional<Catalog> catalog = catalogDb.findById(cat.getIdCatalog());

            if (catalog.isPresent()) {
                result.add(ReadCatalogResponseDTO.builder()
                        .idCatalog(catalog.get().getIdCatalog())
                        .seller(catalog.get().getSeller())
                        .price(catalog.get().getPrice())
                        .productName(catalog.get().getProductName())
                        .productDescription(catalog.get().getProductDescription())
                        .categoryId(catalog.get().getCategory().getIdCategory())
                        .categoryName(catalog.get().getCategory().getCategoryName())
                        .stock(catalog.get().getStock())
                        .isDeleted(catalog.get().getIsDeleted())
                        .image(decompressImage(catalog.get().getImage())).build());
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
    public Catalog updateCatalog (
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

    @Override
    public void saveCatalog(Catalog catalog) {
        catalogDb.save(catalog);
    }

}
