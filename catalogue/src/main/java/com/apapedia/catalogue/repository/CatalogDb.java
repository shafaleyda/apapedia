package com.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apapedia.catalogue.model.Catalog;

import java.util.UUID;
import java.util.List;

public interface CatalogDb extends JpaRepository<Catalog, UUID>{
        List<Catalog> findAll();
        List<Catalog> findByProductNameContainingIgnoreCaseOrderByProductNameAsc(String catalogName);
        List<Catalog> findByPriceOrderByPriceAsc(Integer price);
}

