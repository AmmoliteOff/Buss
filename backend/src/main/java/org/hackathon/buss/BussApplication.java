package org.hackathon.buss;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Buss api")
)
public class BussApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussApplication.class, args);
    }

}
