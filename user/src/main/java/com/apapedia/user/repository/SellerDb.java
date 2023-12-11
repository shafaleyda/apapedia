


package com.apapedia.user.repository;

import com.apapedia.user.model.Seller;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface SellerDb extends JpaRepository<Seller, UUID> {
    List<Seller> findAll();
}