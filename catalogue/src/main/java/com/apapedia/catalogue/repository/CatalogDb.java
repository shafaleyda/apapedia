package com.apapedia.catalogue.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apapedia.catalogue.model.Catalog;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface CatalogDb extends JpaRepository<Catalog, UUID>{
    List<Catalog> findAllByIsDeletedFalseOrderByProductNameAsc();
    
    List<Catalog> findByPriceOrderByPriceAsc(Integer price);
    
    List<Catalog> findBySellerAndIsDeletedFalse(UUID sellerId);
    Optional<Catalog> findByIdCatalogAndIsDeletedFalse(UUID id);

    List<Catalog> findByProductNameContainingIgnoreCaseAndSellerOrderByProductNameAsc(String catalogName, UUID seller);
    List<Catalog> findByProductNameContainingIgnoreCaseOrderByProductNameAsc(String catalogName);

    List<Catalog> findByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Catalog> findByPriceBetweenAndSeller(Integer minPrice, Integer maxPrice, UUID seller);

    List<Catalog> findAll();
    List<Catalog> findAllBySeller(UUID seller, Sort sort);
}

