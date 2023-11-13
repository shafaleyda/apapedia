package com.apapedia.order.repository;

import com.apapedia.order.model.CartItemModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
@Transactional
public interface CartItemDb extends JpaRepository<CartItemModel, UUID>{
    Optional<CartItemModel> findById(UUID id);
    List<CartItemModel> findByCartId(UUID cartId);
}
