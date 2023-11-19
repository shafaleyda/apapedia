package com.apapedia.catalogue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;

import java.util.Random;
import java.util.Locale;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import com.apapedia.catalogue.model.Catalog;
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
			Random random = new Random();
			var faker = new Faker(new Locale("in-ID"));
			int minPrice = 10;
			int maxPrice = 100;
			URL imageUrl = new URL("https://tinyjpg.com/images/social/website.jpg");

			List<String> categoryNameList = Arrays.asList("Aksesoris Fashion", "Buku & Alat Tulis", "Elektronik",
					"Fashion Bayi & Anak", "Fashion Muslim", "Fotografi",
					"Hobi & Koleksi", "Jam Tangan", "Perawatan & Kecantikan",
					"Makanan & Minuman", "Otomotif", "Perlengkapan Rumah", "Souvenir & Party Supplies");

			//Faker category
			for (int i = 0; i <= 12; i++){
				var category = new Category();
				var categoryName = categoryNameList.get(i);
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

				catalog.setSeller(UUID.randomUUID());
				catalog.setPrice(price);
				catalog.setProductName(productName);
				catalog.setProductDescription(productDescription);
				catalog.setCategory(category);
				catalog.setStock(stock);

				try {
					byte[] imageData = imageUrl.openStream().readAllBytes();
					String fileName = StringUtils.cleanPath(Paths.get(imageUrl.getPath()).getFileName().toString());
					if(fileName.contains(".."))
					{
						System.out.println("not a a valid file");
					}
					catalog.setImage(Base64.getEncoder().encodeToString(imageData));
				} catch (IOException e) {
					e.printStackTrace();
				}
				catalogRestService.saveCatalog(catalog);
			}

		};
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}
