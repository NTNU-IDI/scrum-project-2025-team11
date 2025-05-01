package no.ntnu.idatt2106.krisefikser.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
  
  @Bean
  public OpenAPI defineOpenApi() {
    Server server = new Server();
    server.setUrl("http://localhost:8080/");
    server.setDescription("Local server for development and testing");

    Info info = new Info()
        .title("Krisefikser Application API")
        .description("API documentation for endpoints for Krisefikser system");

    // Define the security scheme for a JWT Bearer token
    final String securitySchemeName = "BearerAuth";
    Components components = new Components();
    components.addSecuritySchemes(securitySchemeName, new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("Enter your valid token in the text input below.\n\nExample: \" eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\""));

    // Apply the defined security scheme globally
    SecurityRequirement securityRequirement = new SecurityRequirement();
    securityRequirement.addList(securitySchemeName);

    return new OpenAPI()
        .components(components)
        .addSecurityItem(securityRequirement)
        .info(info)
        .addServersItem(server)
        .tags(List.of(
            new Tag().name("Address").description("Endpoints for address management"),
            new Tag().name("User").description("Endpoints for user management"),
            new Tag().name("Event").description("Endpoints for event management"),
            new Tag().name("Household").description("Endpoints for household management"),
            new Tag().name("Inventory API").description("Endpoints for inventory management"),
            new Tag().name("Item API").description("Endpoints for item management"),
            new Tag().name("Point of interest").description("Endpoints for point of interest management")
        ));
  }
}
