package com.apapedia.catalogue.restservice;

import com.apapedia.catalogue.model.Category;
import java.util.List; 

public interface CategoryRestService {
    void saveCategory(Category category); 
    List<Category> retrieveAllCategory(); 
}
