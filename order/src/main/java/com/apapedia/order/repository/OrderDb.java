package com.apapedia.order.repository;

import com.apapedia.order.model.OrderModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OrderDb extends JpaRepository<OrderModel, UUID>{
    List<OrderModel> findAll();

    List<OrderModel> findAllByCustomer(UUID customer);

    List<OrderModel> findAllBySeller(UUID seller);

    List<OrderModel> findByCreatedAtBetweenAndSeller(LocalDateTime firstDayOfMonth, LocalDateTime now, UUID seller);
}
