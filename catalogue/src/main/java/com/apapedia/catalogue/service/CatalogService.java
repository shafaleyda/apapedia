package com.apapedia.catalogue.service;

import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;

@Service
public interface CatalogService {
    void saveCatalog(Catalog catalog);
}


