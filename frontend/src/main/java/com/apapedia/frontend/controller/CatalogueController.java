package com.apapedia.frontend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.dto.request.UpdateCatalogueRequestDTO;
import com.apapedia.frontend.dto.response.Catalogue;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CatalogueController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/catalog/all")
    public String getCatalog(Model model) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8989/api/catalog/all"))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Parse JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Catalogue[] catalogues = objectMapper.readValue(responseBody, Catalogue[].class);
        System.out.println(catalogues[0].getProductName());
        model.addAttribute("listCatalog", catalogues);
        return "viewall-catalogue";
    }

    @GetMapping("/catalog/create")
    public String formAddBarang(Model model) {
        var catalogDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogDTO", catalogDTO);
        return "form-add-catalogue";
    }

    @PostMapping(value = {"/catalog/create" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addBarang(@ModelAttribute("catalogDTO") CreateCatalogueRequestDTO catalogDTO,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            Model model) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        catalogDTO.setSeller(UUID.randomUUID());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource resource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        body.add("image", resource);
        body.add("model", catalogDTO);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Catalogue> responseEntity = restTemplate.exchange(
                "http://localhost:8989/api/catalog/create",
                HttpMethod.POST,
                requestEntity,
                Catalogue.class);

        return "redirect:/catalog/all";
    }

    @GetMapping("/catalog/update/{id}")
    public String formUpdateBarang(@PathVariable("id") String id, Model model) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8989/api/catalog/" + id))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Parse JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Catalogue catalog = objectMapper.readValue(responseBody, Catalogue.class);
        model.addAttribute("catalogue", catalog);
        return "form-update-catalogue";
    }

    @PostMapping(value = {"/catalog/update/{id}" }, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String updateBarang(@PathVariable("id") String id,
                               @ModelAttribute("catalogue") UpdateCatalogueRequestDTO catalogue,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource resource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        catalogue.setSeller(UUID.randomUUID());

        body.add("image", resource);
        body.add("model", catalogue);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        System.out.println("tes");
        restTemplate.exchange(
                "http://localhost:8989/api/catalog/update/" + id,
                HttpMethod.PUT,
                requestEntity,
                Catalogue.class
        );
        System.out.println("tes response");

        return "redirect:/catalog/all";
    }
}
