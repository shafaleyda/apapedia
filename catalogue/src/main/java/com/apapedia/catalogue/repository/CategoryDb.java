package com.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apapedia.catalogue.model.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryDb extends JpaRepository<Category, UUID> {

    Optional<Category> findByIdCategory(Integer id);

}
