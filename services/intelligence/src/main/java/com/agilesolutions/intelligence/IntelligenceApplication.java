package com.agilesolutions.intelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class IntelligenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligenceApplication.class, args);
	}

}
