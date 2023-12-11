package com.apapedia.frontend.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
@Controller
public class CatalogController {

    String baseUrlCatalogue = "http://localhost:8082"; 
    String baseUrlOrder = "http://localhost:8080"; 
    String baseUrlUser = "http://localhost:8081"; 

    //Guest - View All
    @GetMapping("/catalog/viewall-guest")
    public String viewAllCatalog(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrlCatalogue + "/api/catalog/all";

        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

        model.addAttribute("catalogData", catalogData);
        model.addAttribute("valid", Boolean.TRUE);
        return "guest-viewall-catalog";
    }

    //Seller - View All
    @GetMapping("/catalog/viewall-seller")
    public String sellerViewAllCatalog(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String urlLogin = baseUrlUser + "/api/user/user-loggedin";

        ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class); 
        //System.out.println(userLoggedIn);

        if(userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
            ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> responseBody = userResponse.getBody();
            UUID seller = UUID.fromString(responseBody.get("id").toString()); 
            
            String url = baseUrlCatalogue + "/api/catalog/seller/" + seller.toString();

            //Catalog
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            model.addAttribute("catalogData", catalogData);
            model.addAttribute("valid", Boolean.TRUE);

            //Chart
            String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString(); 
            ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                    urlChart,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<LocalDate, Integer>>() {}
            );

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
        return "seller-viewall-catalog";
    }

    //Guest - Find By Name
    @GetMapping("/catalog/find-by-name")
    public String viewAllCatalogByName(@RequestParam(name = "name", required = false)String name, Model model){
        if (!name.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrlCatalogue + "/api/catalog/view-all-by-name?name=" + name;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);
            
            model.addAttribute("enteredName", name);
 
            if (catalogData.size() > 0) { //Ada catalog dengan nama yang dicari
                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", Boolean.TRUE);
            }

            else { //Tidak ada catalog sesuai dengan nama yang dicari 
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
            }
            return "guest-viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }

    //Seller - Find By Name
    @GetMapping("/catalog/find-by-name-seller")
    public String sellerViewAllCatalogByName(@RequestParam(name = "name", required = false)String name, Model model){
        if (name.length() > 0) {
            RestTemplate restTemplate = new RestTemplate();
            

            String urlLogin = baseUrlUser + "/api/user/user-loggedin";
            ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class); 
            //System.out.println(userLoggedIn);

            if (userLoggedIn.getStatusCode().is2xxSuccessful()) {
                //Catalog Data
                String url = baseUrlCatalogue + "/api/catalog/seller-view-all-by-name?name=" + name;
                List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);
                
                model.addAttribute("enteredName", name);
    
                if (catalogData.size() > 0) { //Ada catalog dengan nama yang dicari
                    model.addAttribute("catalogData", catalogData);
                    model.addAttribute("valid", Boolean.TRUE);
                }

                else { //Tidak ada catalog sesuai dengan nama yang dicari 
                    model.addAttribute("valid", Boolean.FALSE);
                    model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
                }

                //Chart
                ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
                Map<String, Object> responseBody = userResponse.getBody();
                UUID seller = UUID.fromString(responseBody.get("id").toString()); 

                String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString(); 
                ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                        urlChart,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<LocalDate, Integer>>() {}
                );

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
            return "seller-viewall-catalog";
        } else {
            return sellerViewAllCatalog(model);
        }
    }

    //Guest - Price Range
    @GetMapping("/catalog/price-range")
    public String viewAllCatalogByPrice(@RequestParam(name = "minPrice", required = false)String minPrice,
                                        @RequestParam(name = "maxPrice", required = false)String maxPrice,
                                        Model model){
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
                return "guest-viewall-catalog";
            }

            String url = baseUrlCatalogue + "/api/catalog/view-all-by-price?minPrice=" + minPrice + "&maxPrice=" + maxPrice;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            if (catalogData.size() > 0) { //Ada catalog dengan nama yang dicari
                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", Boolean.TRUE);
            } else {
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
            }

            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);

            return "guest-viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }

    //Seller - Price Range
    @GetMapping("/catalog/price-range-seller")
    public String sellerViewAllCatalogByPrice(@RequestParam(name = "minPrice", required = false)String minPrice,
                                        @RequestParam(name = "maxPrice", required = false)String maxPrice,
                                        Model model){
        if (!(minPrice.isEmpty()) && !(maxPrice.isEmpty())) {
            RestTemplate restTemplate = new RestTemplate();
            
            String urlLogin = baseUrlUser + "/api/user/user-loggedin";
            ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class); 

            if (userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
                //Chart
                ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
                Map<String, Object> responseBody = userResponse.getBody();
                UUID seller = UUID.fromString(responseBody.get("id").toString()); 

                
                String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                        urlChart,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<LocalDate, Integer>>() {
                        }
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    Map<LocalDate, Integer> mapTotalOrdersPerDay = response.getBody();
                    Map<String, Integer> salesChartStringKeys = new HashMap<>();
                    for (Map.Entry<LocalDate, Integer> entry : mapTotalOrdersPerDay.entrySet()) {
                        String dateStringKey = entry.getKey().toString();
                        salesChartStringKeys.put(dateStringKey, entry.getValue());
                    }
                    model.addAttribute("listCatalogChart", mapTotalOrdersPerDay);
                }

                //Catalog Data
                int min = minPrice != null ? Integer.parseInt(minPrice) : 0;
                int max = maxPrice != null ? Integer.parseInt(maxPrice) : 0;

                if (min < 0 || max < 0 || min > max) {
                    model.addAttribute("valid", Boolean.FALSE);
                    model.addAttribute("error", "Input tidak valid");
                    if (minPrice != null && maxPrice != null) {
                        model.addAttribute("minPrice", minPrice);
                        model.addAttribute("maxPrice", maxPrice);
                    }
                    return "seller-viewall-catalog";
                }

                String url = baseUrlCatalogue + "/api/catalog/seller-view-all-by-price?minPrice=" + minPrice + "&maxPrice=" + maxPrice;
                List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                if (catalogData.size() > 0) { //Ada catalog dengan nama yang dicari
                    model.addAttribute("catalogData", catalogData);
                    model.addAttribute("valid", Boolean.TRUE);
                } else {
                    model.addAttribute("valid", Boolean.FALSE);
                    model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
                }

                model.addAttribute("minPrice", minPrice);
                model.addAttribute("maxPrice", maxPrice);

                return "seller-viewall-catalog";
            }
        } else {
            return sellerViewAllCatalog(model);
        }
        return null;
    }

    //Guest - Sort By
    @GetMapping("/catalog/sort-by")
    public String sortAllCatalog(@RequestParam(defaultValue = "productName") String sortField,
                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                                 Model model){
        if (sortField != null && sortDirection != null) {
            RestTemplate restTemplate = new RestTemplate();

            String url = baseUrlCatalogue + "/api/catalog/view-all-sort-by?sortField=" + sortField + "&sortDirection=" + sortDirection;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            model.addAttribute("catalogData", catalogData);
            model.addAttribute("valid", true);

            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            return "guest-viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }

    //Seller - Sort By
    @GetMapping("/catalog/sort-by-seller")
    public String sellerSortAllCatalog(@RequestParam(defaultValue = "productName") String sortField,
                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                                 Model model){
        RestTemplate restTemplate = new RestTemplate();

        if (sortField != null && sortDirection != null) {
            
            String urlLogin = baseUrlUser + "/api/user/user-loggedin";
            ResponseEntity<Object> userLoggedIn = restTemplate.getForEntity(urlLogin, Object.class); 

            if (userLoggedIn.getStatusCode().is2xxSuccessful()) { //User login
                //Catalog Data
                String url =  baseUrlCatalogue + "/api/catalog/seller-view-all-sort-by?sortField=" + sortField + "&sortDirection=" + sortDirection;
                List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", true);

                //Chart
                ResponseEntity<Map<String, Object>> userResponse = restTemplate.exchange(
                                                                urlLogin, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
                Map<String, Object> responseBody = userResponse.getBody();
                UUID seller = UUID.fromString(responseBody.get("id").toString()); 
                
                String urlChart = baseUrlOrder + "/order/salesChart/" + seller.toString();
                ResponseEntity<Map<LocalDate, Integer>> response = restTemplate.exchange(
                        urlChart,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<LocalDate, Integer>>() {
                        }
                );

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
                return "seller-viewall-catalog";
            }

        } else {
            return sellerViewAllCatalog(model);
        }
        return null;
    }
}