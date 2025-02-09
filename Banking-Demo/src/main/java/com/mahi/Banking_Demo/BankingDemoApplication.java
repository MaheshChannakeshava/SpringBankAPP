package com.mahi.Banking_Demo;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Bank Application",
				description= "An Bank application using Spring boot and Rest API",
				version = "v1",
				license = @License(
						name = "Mahesh"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Bank Application"
		)
)
public class BankingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingDemoApplication.class, args);
	}

}
