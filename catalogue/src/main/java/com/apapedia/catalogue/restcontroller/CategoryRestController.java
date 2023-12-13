package com.apapedia.catalogue.restcontroller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.catalogue.rest.CategoryRest;
import com.apapedia.catalogue.restservice.CategoryRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.sql.SQLException;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CategoryRestController {
    @Autowired CategoryRestService categoryRestService;

    @GetMapping(value = "/category/view-all")
    @ResponseBody public List<CategoryRest> retrieveAllCategory(HttpServletResponse response) throws SQLException, IOException{
        return categoryRestService.retrieveAllCategoryRest(); 
    }
}
