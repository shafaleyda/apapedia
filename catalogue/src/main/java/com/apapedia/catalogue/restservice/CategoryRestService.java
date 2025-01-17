package com.apapedia.catalogue.restservice;

import com.apapedia.catalogue.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public interface CategoryRestService {
    void saveCategory(Category category);
    List<Category> retrieveAllCategory();
}
