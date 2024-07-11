package com.dcode7.iwell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IwellApplication {

	public static void main(String[] args) {
		SpringApplication.run(IwellApplication.class, args);
	}

}
