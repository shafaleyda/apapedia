package com.apapedia.catalogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apapedia.catalogue.model.Category;

import java.util.Optional;

public interface CategoryDb extends JpaRepository<Category, Integer> {

    Optional<Category> findByIdCategory(Integer id);

}
