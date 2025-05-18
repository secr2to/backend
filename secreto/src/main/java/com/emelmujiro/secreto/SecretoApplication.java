package com.emelmujiro.secreto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SecretoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecretoApplication.class, args);
	}

}

