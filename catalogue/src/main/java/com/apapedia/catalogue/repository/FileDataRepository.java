package com.apapedia.catalogue.repository;

import com.apapedia.catalogue.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<ImageData, Integer> {
}
