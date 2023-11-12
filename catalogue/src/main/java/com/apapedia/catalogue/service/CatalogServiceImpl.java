package com.apapedia.catalogue.service;

import com.apapedia.catalogue.repository.CatalogDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    CatalogDb catalogDb;

    @Override
    public void saveCatalog(Catalog catalog)    {
        catalogDb.save(catalog);
    }


}
