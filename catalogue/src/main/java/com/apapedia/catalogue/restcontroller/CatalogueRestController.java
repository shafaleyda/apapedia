package com.apapedia.catalogue.restcontroller;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalogue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class CatalogueRestController {
    @GetMapping(value="/catalogue/{idCatalogue}")
    private Catalogue retrieveCatalogue(@PathVariable("idCatalogue") Long idCatalogue){
        try{
            // memanggil detail penerbit
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalogue  No " + String.valueOf(idCatalogue) + " Not Found."
            );
        }
        return null;
    }

    @GetMapping(value="/catalogue/{idSeller}")
    private Catalogue retrieveCatalogueBySeller(@PathVariable("idSeller") Long idSeller){
        try{
            // memanggil detail penerbit
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalogue  No " + String.valueOf(idSeller) + " Not Found."
            );
        }
        return null;
    }

    @GetMapping(value="/catalogue/all")
    private Catalogue retrieveAllCatalogue(){
        try{
            // memanggil detail penerbit
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalogue  No " + " Not Found."
            );
        }
        return null;
    }

    @PostMapping(value="/catalogue/create")
    public Catalogue createCatalogue(@Valid @RequestBody CreateCatalogueRequestDTO catalogueDTO, BindingResult bindingResult){
        try{
            // memanggil detail penerbit
//            return penerbitRestService.getRestPenerbitById(idCatalogue);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Catalogue  No " + " Not Found."
            );
        }
        return null;
    }
}

