package com.apapedia.order.repository;

import com.apapedia.order.model.CartModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.Optional;

@Repository
@Transactional
public interface CartDb extends JpaRepository<CartModel, UUID>{
    Optional<CartModel> findByUserId(UUID userId);
}
