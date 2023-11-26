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
//@RestController
public class CatalogController {

    @GetMapping("/catalog/viewall")
    public String viewAllCatalog(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/api/catalog/all";
        List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);
        model.addAttribute("catalogData", catalogData);
        model.addAttribute("valid", Boolean.TRUE);

        //MASIH HARDCODE
        String urlChart = "http://localhost:8081/order/salesChart/" + UUID.fromString("34356813-fcbd-4872-9ca4-ae26a81c092c");
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
        return "viewall-catalog";
    }

    @GetMapping("/catalog/find-by-name")
    public String viewAllCatalogByName(@RequestParam(name = "name", required = false)String name, Model model){
        if (name != null) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8082/api/catalog/view-all-by-name?name=" + name;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            if (catalogData.size() > 0) { //Ada catalog dengan nama yang dicari
                model.addAttribute("catalogData", catalogData);
                model.addAttribute("valid", Boolean.TRUE);
            } else {
                if (name != null) {
                    model.addAttribute("enteredName", name);
                }
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Tidak ada produk yang sesuai pencarian Anda.");
            }


            //MASIH HARDCODE
            String urlChart = "http://localhost:8081/order/salesChart/" + UUID.fromString("34356813-fcbd-4872-9ca4-ae26a81c092c");
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


            model.addAttribute("enteredName", name);

            return "viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }

    @GetMapping("/catalog/price-range")
    public String viewAllCatalogByPrice(@RequestParam(name = "minPrice", required = false)String minPrice,
                                        @RequestParam(name = "maxPrice", required = false)String maxPrice,
                                        Model model){
        if (!(minPrice.equalsIgnoreCase("")) && !(maxPrice.equalsIgnoreCase(""))) {
            RestTemplate restTemplate = new RestTemplate();
            //MASIH HARDCODE
            String urlChart = "http://localhost:8081/order/salesChart/" + UUID.fromString("34356813-fcbd-4872-9ca4-ae26a81c092c");
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

            int min = minPrice != null ? Integer.parseInt(minPrice) : 0;
            int max = maxPrice != null ? Integer.parseInt(maxPrice) : 0;

            if (min < 0 || max < 0 || min > max) {
                model.addAttribute("valid", Boolean.FALSE);
                model.addAttribute("error", "Input tidak valid");
                if (minPrice != null && maxPrice != null) {
                    model.addAttribute("minPrice", minPrice);
                    model.addAttribute("maxPrice", maxPrice);
                }
                return "viewall-catalog";
            }


            String url = "http://localhost:8082/api/catalog/view-all-by-price?minPrice=" + minPrice + "&maxPrice=" + maxPrice;
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

            return "viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }

    @GetMapping("/catalog/sort-by")
    public String sortAllCatalog(@RequestParam(defaultValue = "productName") String sortField,
                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                                 Model model){
        if (sortField != null && sortDirection != null) {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8082/api/catalog/view-all-sort-by?sortField=" + sortField + "&sortDirection=" + sortDirection;
            List<Map<String, Object>> catalogData = restTemplate.getForObject(url, List.class);

            model.addAttribute("catalogData", catalogData);
            model.addAttribute("valid", true);

            //MASIH HARDCODE
            String urlChart = "http://localhost:8081/order/salesChart/" + UUID.fromString("34356813-fcbd-4872-9ca4-ae26a81c092c");
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
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            return "viewall-catalog";
        } else {
            return viewAllCatalog(model);
        }
    }
}

