package com.apapedia.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apapedia.order.model.OrderModel;

public interface OrderDb extends JpaRepository<OrderModel, UUID>{
    List<OrderModel> findAll();

    List<OrderModel> findAllByCustomer(UUID customer);

    List<OrderModel> findAllBySeller(UUID seller);
}
