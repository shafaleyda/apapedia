package com.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apapedia.catalogue.model.Catalog;

import java.util.UUID;
import java.util.List;

public interface CatalogDb extends JpaRepository<Catalog, UUID>{
    List<Catalog> findAll();
    List<Catalog> findByProductNameContainingIgnoreCaseOrderByProductNameAsc(String catalogName);
    List<Catalog> findByPriceOrderByPriceAsc(Integer price);
    List<Catalog> findByPriceBetween(Integer minPrice, Integer maxPrice);
}

