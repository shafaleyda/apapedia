//package com.apapedia.catalogue.controller;
//
//import com.apapedia.catalogue.dto.mapper.CatalogMapper;
//import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
////import com.apapedia.catalogue.model.ImageData;
//import com.apapedia.catalogue.restservice.CatalogRestService;
//import com.apapedia.catalogue.service.CatalogService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Controller
//public class CatalogController {
//    @Autowired
//    private CatalogMapper catalogMapper;
//
//    @Autowired
//    private CatalogRestService catalogRestService;
//
//    @Autowired
//    private CatalogService catalogService;
//
//    @GetMapping("/catalog/create")
//    public String formAddBarang(Model model) {
//        var catalogDTO = new CreateCatalogueRequestDTO();
//        model.addAttribute("catalogDTO", catalogDTO);
//        return "form-add-catalog";
//    }
//
////    @PostMapping(value={"/catalog/create"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
////    public String createCatalogue(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogDTO,
////                                  BindingResult bindingResult,
////                                  @RequestParam("image") MultipartFile[] imageFiles,
////                                  Model model)
////            throws IOException {
////
////        var catalog = catalogMapper.createCatalogRequestDTOToCatalogModel(catalogDTO);
////        catalogRestService.createRestCatalog(catalog, imageFiles, jsonObject);
////        return "success-create-catalog";
////    }
//
//}

