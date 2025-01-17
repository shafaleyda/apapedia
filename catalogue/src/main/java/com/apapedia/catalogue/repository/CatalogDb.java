package com.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apapedia.catalogue.model.Catalog;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface CatalogDb extends JpaRepository<Catalog, UUID>{
    List<Catalog> findAll();
    List<Catalog> findAllByIsDeletedFalseOrderByProductNameAsc();
    List<Catalog> findByProductNameContainingIgnoreCaseOrderByProductNameAsc(String catalogName);
    List<Catalog> findByPriceOrderByPriceAsc(Integer price);
    List<Catalog> findByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Catalog> findBySellerAndIsDeletedFalse(UUID sellerId);
    Optional<Catalog> findByIdCatalogAndIsDeletedFalse(UUID id);

}

