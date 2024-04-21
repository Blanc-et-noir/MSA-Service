package com.spring.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class VerificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerificationServiceApplication.class, args);
	}

}
