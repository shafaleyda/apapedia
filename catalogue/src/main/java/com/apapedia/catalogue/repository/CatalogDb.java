package com.apapedia.catalogue.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apapedia.catalogue.model.Catalog;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
@Transactional
public interface CatalogDb extends JpaRepository<Catalog, UUID>{
    List<Catalog> findAll();
//    List<Catalog> findByAllOrderByProductNameAsc();
//    List<Catalog> findByOrderByProductPriceAsc(int productPrice);
//    Optional<Catalog> findBySellerId(UUID sellerId);

}

