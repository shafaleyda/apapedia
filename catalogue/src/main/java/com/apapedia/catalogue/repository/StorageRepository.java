package com.apapedia.catalogue.repository;

import com.apapedia.catalogue.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findByName(String fileName);
}