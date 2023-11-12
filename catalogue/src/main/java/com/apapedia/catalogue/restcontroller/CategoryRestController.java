package com.apapedia.catalogue.restcontroller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.catalogue.rest.CatalogRest;
import com.apapedia.catalogue.restservice.CategoryRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Hashtable;
import java.util.Dictionary;
import java.util.List;
import java.sql.SQLException;
import java.io.IOException;

import com.apapedia.catalogue.model.Category;

@RestController
@RequestMapping("/api")
public class CategoryRestController {
    @Autowired CategoryRestService categoryRestService;

    @GetMapping(value = "/category/view-all")
    @ResponseBody public ResponseEntity<Dictionary<String, Object>> retrieveAllCategory(HttpServletResponse response) throws SQLException, IOException{
        Dictionary<String, Object> responseData= new Hashtable<>();
        List<Category> listAllCategory = categoryRestService.retrieveAllCategory();
            responseData.put("status", HttpStatus.OK.value());
            responseData.put("data", listAllCategory);
            responseData.put("message", "success"); 
            return ResponseEntity.status(HttpStatus.OK)
            .body(responseData);
    }
}
