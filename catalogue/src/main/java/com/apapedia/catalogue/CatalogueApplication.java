package com.apapedia.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication()
public class CatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

}
