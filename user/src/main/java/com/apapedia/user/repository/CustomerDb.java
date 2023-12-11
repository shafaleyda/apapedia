



package com.apapedia.user.repository;

import com.apapedia.user.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface CustomerDb extends JpaRepository<Customer, UUID> {
    List<Customer> findAll();
}