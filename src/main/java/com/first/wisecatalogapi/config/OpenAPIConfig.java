package com.first.wisecatalogapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "📚 Wise Catalog API",
                version = "1.0.0",
                description = "API REST para gerenciamento e busca de livros da Amazon.",
                contact = @Contact(name = "Fábio Ferreira", email = "fabio_junior1994@hotmail.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        )
)
public class OpenAPIConfig {
}
