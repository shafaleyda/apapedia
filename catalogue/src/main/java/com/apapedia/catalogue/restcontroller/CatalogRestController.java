package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.dto.mapper.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.restservice.CatalogRestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CatalogRestController {
    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private CatalogRestService catalogRestService;

    @GetMapping(value="/catalog/{idCatalog}")
    private Catalog retrieveCatalogue(@PathVariable("idCatalog") UUID idCatalog){
        try{
            return catalogRestService.getRestCatalogById(idCatalog);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog  No " + String.valueOf(idCatalog) + " Not Found."
            );
        }
    }

    @GetMapping(value="/catalog/seller/{idSeller}")
    private Catalog retrieveCatalogueBySellerId(@PathVariable("idSeller") Long idSeller){
        try{
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog  No " + String.valueOf(idSeller) + " Not Found."
            );
        }
        return null;
    }

    @GetMapping(value="/catalog/all")
    private List<Catalog> retrieveAllCatalogue(){
        return catalogRestService.retrieveRestAllCatalog();

    }

    @PostMapping(value="/catalog/create")
    public Catalog createRestCatalogue(@Valid @RequestBody CreateCatalogueRequestDTO catalogDTO, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            var catalog = catalogMapper.createCatalogRequestDTOToCartModel(catalogDTO);
            catalogRestService.createRestCatalog(catalog);
            return catalog;
        }
    }

    @PutMapping(value = "/catalog/{idCatalog}")
    private Catalog updateRestPenerbit(
            @PathVariable("idCatalog") UUID idCatalog,
            @RequestBody UpdateCatalogRequestDTO updateCatalogRequestDTO) {
        try {
            return catalogRestService.updateCatalog(idCatalog, updateCatalogRequestDTO);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalog No" + idCatalog + " Not Found!"
            );

        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, " "
            );
        }
    }

}

