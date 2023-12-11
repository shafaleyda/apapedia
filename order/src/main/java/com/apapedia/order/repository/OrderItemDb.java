package com.apapedia.order.repository;

import com.apapedia.order.model.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
@Transactional
public interface OrderItemDb extends JpaRepository<OrderItemModel, UUID>{
    Optional<OrderItemModel> findById(UUID id);
    List<OrderItemModel> findByOrderId(UUID orderId);
}
