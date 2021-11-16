package io.temp.file.storage.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class StoreApplication {

	/**
	 * ? you'll find that any code that didn't work or is no longer needed is ?
	 * commented out
	 */

	 // ! swagger url: localhost:8080/swagger-ui/#
	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

}
