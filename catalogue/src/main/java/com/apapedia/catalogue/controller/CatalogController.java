package com.apapedia.catalogue.controller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.rest.CatalogRest;
//import com.apapedia.catalogue.model.ImageData;
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

@Controller
public class CatalogController {
    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private CatalogRestService catalogRestService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/catalog/create")
    public String formAddBarang(Model model) {
        var catalogDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogDTO", catalogDTO);
        return "form-add-catalog";
    }

    @GetMapping("/catalog/viewall")
    public String viewAllCatalog(Model model){
        List<CatalogRest> listCatalogRest = catalogService.getAllCatalog(); 

        List<ReadCatalogResponseDTO> listReadCatalogResponseDTO = new ArrayList<>(); 
        for (CatalogRest catalogRest: listCatalogRest) {
            var readCatalogDTO = catalogMapper.catalogRestToReadCatalogResponseDTO(catalogRest); 
            listReadCatalogResponseDTO.add(readCatalogDTO); 
        }
		model.addAttribute("listCatalog", listReadCatalogResponseDTO);
		return "viewall-catalog";
    }

}

