package com.datahub.metadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.datahub.metadata")
public class CustomerMetadataApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerMetadataApplication.class, args);
	}

}
