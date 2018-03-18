package com.shbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShbsApplication.class, args);
	}
}
