package com.apapedia.catalogue.controller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.model.Image;
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CatalogController {
    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private CatalogRestService catalogRestService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping(value={"/catalog/create"}, produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String formAddBarang(Model model) {
        //Membuat DTO baru sebagai isian form pengguna
        var catalogDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogDTO", catalogDTO);

        return "form-add-catalog";
    }

    @PostMapping(value={"/catalog/create"}, produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createCatalogue(@Valid @RequestBody CreateCatalogueRequestDTO catalogDTO,
                                  BindingResult bindingResult,
                                  @RequestPart("imageFile") MultipartFile[] imageFiles,
                                  Model model)
            throws IOException
    {
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        }
        // Upload images and create a catalog
        Set<Image> imageModel = new HashSet<>();
        for (MultipartFile file : imageFiles) {
            Image imageModels = new Image(
                    file.getOriginalFilename(), file.getContentType(), file.getBytes());
            imageModel.add(imageModels);
        }
        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
        catalogService.saveCatalog(catalog);
        return "success-create-catalog";
    }

}