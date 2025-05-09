package no.ntnu.idatt2106.krisefikser.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;


/**
* Swagger configuration class for the application.
* This class configures OpenAPI settings, including API information and server details.
*/
@Configuration
public class SwaggerConfig {
    
    /**
    * Configures the OpenAPI documentation for the application.
    *
    * @return the configured OpenAPI object
    */
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Local server for development and testing");
        
        Info info = new Info()
        .title("Krisefikser Application API")
        .description("API documentation for endpoints for Krisefikser system");
        
        // Define security scheme for JWT cookies
        Components components = new Components()
        .addSecuritySchemes("jwtCookieAuth", new SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.COOKIE)
        .name("jwtToken") // Name of your JWT cookie
        .description("JWT token in cookie")
        );
        
        return new OpenAPI()
        .components(components)
        .info(info)
        .addServersItem(server)
        //.addSecurityItem(new SecurityRequirement().addList("jwtCookieAuth"))
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

