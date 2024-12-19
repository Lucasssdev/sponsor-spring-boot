package com.example.sponsors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.sponsors")
public class SponsorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SponsorsApplication.class, args);
	}
}
