package com.apapedia.catalogue.restservice;


import java.util.List;
import java.util.ArrayList; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.repository.CategoryDb;
import com.apapedia.catalogue.rest.CategoryRest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryRestServiceImpl implements com.apapedia.catalogue.restservice.CategoryRestService {
    @Autowired
    private CategoryDb categoryDb;

    @Override
    public void saveCategory(Category category) {
        categoryDb.save(category);
    }

    @Override
    public List<Category> retrieveAllCategory() {
        return categoryDb.findAll();
    }

    @Override
    public List<CategoryRest> retrieveAllCategoryRest() {
       List<CategoryRest> result =  new ArrayList<>();

       for (Category cat: retrieveAllCategory()) {
        CategoryRest categoryRest = new CategoryRest(); 

        categoryRest.setIdCategory(cat.getIdCategory());
        categoryRest.setCategoryName(cat.getCategoryName());
        result.add(categoryRest);
       }
       return result; 

    }


}
