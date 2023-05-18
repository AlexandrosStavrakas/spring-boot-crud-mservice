package gr.crud.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact()
                .email("alexandros.stavrakas1990@gmail.com")
                .name("Alexandros Stavrakas")
                .url("https://www.linkedin.com/in/alexandros-stavrakas-35b88b67/");

        Info info = new Info()
                .contact(contact)
                .title("Vodafone crud assessment");

        return new OpenAPI().info(info);
    }
}
