package org.pet.crypto;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Crypto pet project", version = "1.0", description = "Documentation APIs v1.0"))
public class App {
    public static void main(String ... args) {
        SpringApplication.run(App.class, args);
    }
}
