package com.apapedia.catalogue;

import com.apapedia.catalogue.restservice.CatalogRestService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}
//
//	@Bean
//	@Transactional
//	CommandLineRunner run(CatalogRestService bukuService, PenerbitService penerbitService, BukuMapper bukuMapper, PenerbitMapper penerbitMapper) {
//		return arg -> {

}
