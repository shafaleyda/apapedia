package com.apapedia.catalogue.controller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.dto.response.ReadCatalogResponseDTO;
import com.apapedia.catalogue.rest.CatalogRest;
//import com.apapedia.catalogue.model.ImageData;
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.service.CatalogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
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

    @PostMapping("/catalog/create")
    public String createCatalog(@ModelAttribute("catalogRest")
                                @Valid CreateCatalogueRequestDTO catalogRest,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model) throws Exception {


        //HARUSNYA dari AUTH
        catalogRest.setSeller(UUID.randomUUID());

        // tidak perlu mengembalikan list karena api hanya mengembalikan 1 object data
        catalogService.createCatalog(catalogRest, imageFile);

        return "redirect:/catalog/viewall";
    }

    @GetMapping("/catalog/update/{catalogId}")
    public String updateCatalog(@PathVariable String catalogId, Model model){
        var catalog = catalogRestService.getCatalogById(catalogId);


        System.out.println(catalog);

        model.addAttribute("data", catalog);

        return "form-update-catalog";
    }

    @PostMapping("/catalog/update/{catalogId}")
    public String ubahBarang(@ModelAttribute("data")
                             @Valid UpdateCatalogRequestDTO updateCatalogRequestDto,
                             @PathVariable("catalogId") UUID id,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) throws Exception {


        System.out.println(imageFile);
        updateCatalogRequestDto.setSeller(UUID.randomUUID());

        catalogService.updateCatalog(id, updateCatalogRequestDto, imageFile);

        return "redirect:/catalog/viewall";
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

