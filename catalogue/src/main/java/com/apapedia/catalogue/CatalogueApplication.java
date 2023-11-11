package com.apapedia.catalogue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;


import java.util.Random;

import java.util.Locale;
import java.util.List; 

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.restservice.CatalogRestService;
import com.apapedia.catalogue.restservice.CategoryRestService;
import com.apapedia.catalogue.utils.ImageUtil;

@SpringBootApplication()
@ComponentScan(basePackages = "com.apapedia.catalogue")
public class CatalogueApplication {

	@Autowired
	ImageUtil imageUtil; 
	
	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run (CatalogRestService catalogRestService, CategoryRestService categoryRestService) {
		return args -> {
			Random random = new Random();
			var faker = new Faker(new Locale("in-ID")); 
			int minPrice = 10;
        	int maxPrice = 100;
			String imageUrl = "https://tinyjpg.com/images/social/website.jpg"; 
			byte[] compressedImage = imageUtil.fetchAndCompressImage(imageUrl); 

			//Faker category
			for (int i = 0; i <= 9; i++){
				var category = new Category(); 
				var categoryName = faker.animal().name();

				category.setCategoryName(categoryName);
				categoryRestService.saveCategory(category);
			}

			//Faker Catalog 
			for (int i = 0; i <= 9; i++){
				var catalog = new Catalog(); 

				var price = random.nextInt((maxPrice - minPrice) + 1) + minPrice; 
				var productName = faker.commerce().productName(); 
				var productDescription = faker.commerce().material(); 

				List<Category> listCategory = categoryRestService.retrieveAllCategory(); 
				int randomIndex = random.nextInt(listCategory.size());
				var category = listCategory.get(randomIndex); 
				var stock = random.nextInt(100); 

				catalog.setPrice(price);
				catalog.setProductName(productName);
				catalog.setProductDescription(productDescription);
				catalog.setCategory(category);
				catalog.setStock(stock);
				catalog.setImage(compressedImage);

				catalogRestService.saveCatalog(catalog);
			}

		};
	}

}
