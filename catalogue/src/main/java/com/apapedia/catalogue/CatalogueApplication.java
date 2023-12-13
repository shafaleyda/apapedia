package com.apapedia.catalogue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Arrays;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.restservice.CategoryRestService;

@SpringBootApplication()
//@ComponentScan(basePackages = "com.apapedia.catalogue")
public class CatalogueApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run (CatalogRestService catalogRestService, CategoryRestService categoryRestService) {
		return args -> {
			List<String> categoryNameList = Arrays.asList("Aksesoris Fashion", "Buku & Alat Tulis", "Elektronik",
					"Fashion Bayi & Anak", "Fashion Muslim", "Fotografi",
					"Hobi & Koleksi", "Jam Tangan", "Perawatan & Kecantikan",
					"Makanan & Minuman", "Otomotif", "Perlengkapan Rumah", "Souvenir & Party Supplies");

			if (categoryRestService.retrieveAllCategory().size() < 1) {
				for (int i = 0; i <= 12; i++){
				var category = new Category();
				var categoryName = categoryNameList.get(i);
				category.setCategoryName(categoryName);
				categoryRestService.saveCategory(category);
				}
			}
		};
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}
