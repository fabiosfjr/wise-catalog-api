package com.first.wisecatalogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WiseCatalogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiseCatalogApiApplication.class, args);
	}

}
