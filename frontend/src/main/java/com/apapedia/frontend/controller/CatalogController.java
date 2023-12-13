package com.apapedia.frontend.controller;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.dto.request.UpdateCatalogueRequestDTO;
import com.apapedia.frontend.dto.response.Catalogue;
import com.apapedia.frontend.dto.response.UserId;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

@Controller
public class CatalogController {
    @Autowired
    UserController userController;

    String baseUrlCatalogue = "http://localhost:8082";
    String baseUrlOrder = "http://localhost:8080";
    String baseUrlUser = "http://localhost:8081";

    // Guest - Find By Name
    @GetMapping("/catalog/find-by-name")
    public String viewAllCatalogByName(@RequestParam(name = "name", required = false) String name, Model model) {
        if (!name.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrlCatalogue + "/api/catalog/view-all-by-name?name=" + name;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            model.addAttribute("enteredName", name);

            if (catalogData.size() > 0) { // Ada catalog dengan nama yang dicari
                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", Boolean.TRUE);
            }

            else { // Tidak ada catalog sesuai dengan nama yang dicari
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
            }
            return "catalog/guest-viewall-catalog";
        } else {
            return userController.dashboardSellerGuest(model);
        }
    }

    // Seller - Find By Name
    @GetMapping("/catalog/find-by-name-seller")
    public String sellerViewAllCatalogByName(@RequestParam(name = "name", required = false) String name, Model model,
            HttpServletRequest httpServletRequest) throws IOException, InterruptedException {

        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                if (name.length() > 0) {
                    RestTemplate restTemplate = new RestTemplate();

                    String urlLogin = baseUrlUser + "/api/user/user-loggedin";
                    ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class);
                    // System.out.println(userLoggedIn);

                    if (userLoggedIn.getStatusCode().is2xxSuccessful()) {
                        // Catalog Data
                        String url = baseUrlCatalogue + "/api/catalog/seller-view-all-by-name?name=" + name;
                        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                        model.addAttribute("enteredName", name);

                        if (catalogData.size() > 0) { // Ada catalog dengan nama yang dicari
                            model.addAttribute("catalogData", catalogData);
                            model.addAttribute("valid", Boolean.TRUE);
                        }

                        else { // Tidak ada catalog sesuai dengan nama yang dicari
                            model.addAttribute("valid", Boolean.FALSE);
                            model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
                        }

                        // Chart
                        ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                                });
                        Map<String, Object> responseBody = userResponse.getBody();
                        UUID seller = UUID.fromString(responseBody.get("id").toString());

                        String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                        ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                                urlChart,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Map<LocalDate, Integer>>() {
                                });

                        if (response.getStatusCode().is2xxSuccessful()) {
                            Map<LocalDate, Integer> mapTotalOrdersPerDay = response.getBody();
                            Map<String, Integer> salesChartStringKeys = new HashMap<>();
                            for (Map.Entry<LocalDate, Integer> entry : mapTotalOrdersPerDay.entrySet()) {
                                String dateStringKey = entry.getKey().toString();
                                salesChartStringKeys.put(dateStringKey, entry.getValue());
                            }
                            model.addAttribute("listCatalogChart", mapTotalOrdersPerDay);
                        }
                    }
                    return "catalog/seller-viewall-catalog";
                } else {
                    return userController.dashboardSeller(model, httpServletRequest);
                }
            }
        }
        return "catalog/seller-viewall-catalog";

    }

    // Guest - Price Range
    @GetMapping("/catalog/price-range")
    public String viewAllCatalogByPrice(@RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            Model model) {
        if (!(minPrice.isEmpty()) && !(maxPrice.isEmpty())) {
            RestTemplate restTemplate = new RestTemplate();

            int min = minPrice != null ? Integer.parseInt(minPrice) : 0;
            int max = maxPrice != null ? Integer.parseInt(maxPrice) : 0;

            if (min < 0 || max < 0 || min > max) {
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Input tidak valid");
                if (minPrice != null && maxPrice != null) {
                    model.addAttribute("minPrice", minPrice);
                    model.addAttribute("maxPrice", maxPrice);
                }
                return "catalog/guest-viewall-catalog";
            }

            String url = baseUrlCatalogue + "/api/catalog/view-all-by-price?minPrice=" + minPrice + "&maxPrice="
                    + maxPrice;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            if (catalogData.size() > 0) { // Ada catalog dengan nama yang dicari
                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", Boolean.TRUE);
            } else {
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
            }

            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);

            return "catalog/guest-viewall-catalog";
        } else {
            return userController.dashboardSellerGuest(model);
        }
    }

    // Seller - Price Range
    @GetMapping("/catalog/price-range-seller")
    public String sellerViewAllCatalogByPrice(@RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice,
            Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                if (!(minPrice.isEmpty()) && !(maxPrice.isEmpty())) {
                    RestTemplate restTemplate = new RestTemplate();

                    String urlLogin = baseUrlUser + "/api/user/user-loggedin";
                    ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class);

                    if (userLoggedIn.getStatusCode().is2xxSuccessful()) { // User login
                        // Chart
                        ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                                });
                        Map<String, Object> responseBody = userResponse.getBody();
                        UUID seller = UUID.fromString(responseBody.get("id").toString());

                        String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                        ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                                urlChart,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Map<LocalDate, Integer>>() {
                                });

                        if (response.getStatusCode().is2xxSuccessful()) {
                            Map<LocalDate, Integer> mapTotalOrdersPerDay = response.getBody();
                            Map<String, Integer> salesChartStringKeys = new HashMap<>();
                            for (Map.Entry<LocalDate, Integer> entry : mapTotalOrdersPerDay.entrySet()) {
                                String dateStringKey = entry.getKey().toString();
                                salesChartStringKeys.put(dateStringKey, entry.getValue());
                            }
                            model.addAttribute("listCatalogChart", mapTotalOrdersPerDay);
                        }

                        // Catalog Data
                        int min = minPrice != null ? Integer.parseInt(minPrice) : 0;
                        int max = maxPrice != null ? Integer.parseInt(maxPrice) : 0;

                        if (min < 0 || max < 0 || min > max) {
                            model.addAttribute("valid", Boolean.FALSE);
                            model.addAttribute("error", "Input tidak valid");
                            if (minPrice != null && maxPrice != null) {
                                model.addAttribute("minPrice", minPrice);
                                model.addAttribute("maxPrice", maxPrice);
                            }
                            return "catalog/seller-viewall-catalog";
                        }

                        String url = baseUrlCatalogue + "/api/catalog/seller-view-all-by-price?minPrice=" + minPrice
                                + "&maxPrice=" + maxPrice;
                        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                        if (catalogData.size() > 0) { // Ada catalog dengan nama yang dicari
                            model.addAttribute("catalogData", catalogData);
                            model.addAttribute("valid", Boolean.TRUE);
                        } else {
                            model.addAttribute("valid", Boolean.FALSE);
                            model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
                        }

                        model.addAttribute("minPrice", minPrice);
                        model.addAttribute("maxPrice", maxPrice);

                        return "catalog/seller-viewall-catalog";
                    }
                } else {
                    return userController.dashboardSeller(model, httpServletRequest);
                }

            }
        }
        return "catalog/seller-viewall-catalog";
    }

    // Guest - Sort By
    @GetMapping("/catalog/sort-by")
    public String sortAllCatalog(@RequestParam(defaultValue = "productName") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Model model) {
        if (sortField != null && sortDirection != null) {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseUrlCatalogue + "/api/catalog/view-all-sort-by?sortField=" + sortField + "&sortDirection="
                    + sortDirection;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            model.addAttribute("catalogData", catalogData);
            model.addAttribute("valid", true);

            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            return "catalog/guest-viewall-catalog";
        } else {
            return userController.dashboardSellerGuest(model);
        }
    }

    // Seller - Sort By
    @GetMapping("/catalog/sort-by-seller")
    public String sellerSortAllCatalog(@RequestParam(defaultValue = "productName") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {

        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return "user/access-denied.html";
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                RestTemplate restTemplate = new RestTemplate();

                if (sortField != null && sortDirection != null) {

                    String urlLogin = baseUrlUser + "/api/user/user-loggedin";
                    ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class);

                    if (userLoggedIn.getStatusCode().is2xxSuccessful()) { // User login
                        // Catalog Data
                        String url = baseUrlCatalogue + "/api/catalog/seller-view-all-sort-by?sortField=" + sortField
                                + "&sortDirection=" + sortDirection;
                        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                        model.addAttribute("catalogData", catalogData);
                        model.addAttribute("valid", true);

                        // Chart
                        ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                                });
                        Map<String, Object> responseBody = userResponse.getBody();
                        UUID seller = UUID.fromString(responseBody.get("id").toString());

                        String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                        ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                                urlChart,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Map<LocalDate, Integer>>() {
                                });

                        if (response.getStatusCode().is2xxSuccessful()) {
                            Map<LocalDate, Integer> mapTotalOrdersPerDay = response.getBody();
                            Map<String, Integer> salesChartStringKeys = new HashMap<>();
                            for (Map.Entry<LocalDate, Integer> entry : mapTotalOrdersPerDay.entrySet()) {
                                String dateStringKey = entry.getKey().toString();
                                salesChartStringKeys.put(dateStringKey, entry.getValue());
                            }
                            model.addAttribute("listCatalogChart", mapTotalOrdersPerDay);
                        }

                        model.addAttribute("sortField", sortField);
                        model.addAttribute("sortDirection", sortDirection);
                        return "catalog/seller-viewall-catalog";
                    }

                } else {
                    return userController.dashboardSeller(model, httpServletRequest);
                }

            }
        }
        return "catalog/seller-viewall-catalog";
    }

    public boolean isUserLoggedId(HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if (!("jwtToken".equals(cookie.getName()))) {
                continue;
            } else {
                RestTemplate restTemplate = new RestTemplate();
                String urlLogin = baseUrlUser + "/api/user/user-loggedin";

                ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class);

                if (userLoggedIn.getStatusCode().is2xxSuccessful()) { // User login
                    return true;
                }
            }
        }
        return false;
    }

    // Seller - Add Catalog GET
    @GetMapping("/catalog/create")
    public String formAddBarang(Model model, HttpServletRequest httpServletRequest)
            throws IOException, InterruptedException {
        boolean isUserLoggedIn = isUserLoggedId(httpServletRequest);

        if (!isUserLoggedIn) {
            return "user/access-denied.html";
        }

        var catalogDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogDTO", catalogDTO);
        return "form-add-catalogue";
    }

    // Seller - Add Catalog POST
    @PostMapping(value = { "/catalog/create" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String addBarang(@ModelAttribute("catalogDTO") CreateCatalogueRequestDTO catalogDTO,
            HttpServletRequest httpServletRequest,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws IOException, InterruptedException {

        boolean isUserLoggedIn = isUserLoggedId(httpServletRequest);

        if (!isUserLoggedIn) {
            return "user/access-denied.html";
        }

        RestTemplate restTemplate = new RestTemplate();
        String getUserIdUrl = baseUrlUser + "/api/user/user-id";

        ResponseEntity<UserId> responseEntity = restTemplate.getForEntity(getUserIdUrl, UserId.class);

        UserId user = responseEntity.getBody();
        catalogDTO.setSeller(UUID.fromString(user.getUserId()));

        restTemplate = new RestTemplate();

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

        restTemplate.exchange(
                baseUrlCatalogue + "/api/catalog/create",
                HttpMethod.POST,
                requestEntity,
                Catalogue.class);

        return "redirect:/dashboard/seller";
    }

    // Seller - Update Catalog GET
    @GetMapping("/catalog/update/{id}")
    public String formUpdateBarang(@PathVariable("id") String id, Model model, HttpServletRequest httpServletRequest)
            throws IOException, InterruptedException {

        boolean isUserLoggedIn = isUserLoggedId(httpServletRequest);

        if (!isUserLoggedIn) {
            return "user/access-denied.html";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrlCatalogue + "/api/catalog/" + id))
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

    // Seller - Update Catalog POST
    @PostMapping(value = { "/catalog/update/{id}" }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String updateBarang(@PathVariable("id") String id,
            @ModelAttribute("catalogue") UpdateCatalogueRequestDTO catalogue,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws IOException, InterruptedException {

        RestTemplate restTemplate = new RestTemplate();
        String getUserIdUrl = baseUrlUser + "/api/user/user-id";

        ResponseEntity<UserId> responseEntity = restTemplate.getForEntity(getUserIdUrl, UserId.class);

        UserId user = responseEntity.getBody();
        catalogue.setSeller(UUID.fromString(user.getUserId()));

        HttpHeaders headers = new HttpHeaders();

        restTemplate = new RestTemplate();

        restTemplate = new RestTemplate();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource resource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        };

        // catalogue.setSeller(UUID.randomUUID());

        body.add("image", resource);
        body.add("model", catalogue);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(
                baseUrlCatalogue + "/api/catalog/update/" + id,
                HttpMethod.PUT,
                requestEntity,
                Catalogue.class);

        return "redirect:/dashboard/seller";
    }

}