package com.apapedia.order.repository;

import com.apapedia.order.model.OrderModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface OrderDb extends JpaRepository<OrderModel, UUID>{
}
