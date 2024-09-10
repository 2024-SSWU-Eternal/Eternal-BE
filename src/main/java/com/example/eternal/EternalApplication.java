package com.example.eternal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(EternalApplication.class, args);
	}

}
