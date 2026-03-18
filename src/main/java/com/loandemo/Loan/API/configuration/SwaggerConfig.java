package com.loandemo.Loan.API.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger (OpenAPI 3) documentation.
 *
 * <p>This class defines the OpenAPI specification for the Loan Management System.
 * It customizes API metadata such as title, version, description, and contact details,
 * and also configures JWT-based authentication for secured endpoints.</p>
 *
 * <p>Main Features:</p>
 * <ul>
 *     <li>Provides API metadata (title, version, description)</li>
 *     <li>Includes developer contact information</li>
 *     <li>Configures JWT Bearer authentication scheme</li>
 *     <li>Applies global security requirement for all endpoints</li>
 * </ul>
 *
 * <p>Security Configuration:</p>
 * <ul>
 *     <li>Uses HTTP Bearer authentication</li>
 *     <li>Token format: JWT</li>
 *     <li>Header: Authorization</li>
 *     <li>Example: Authorization: Bearer &lt;token&gt;</li>
 * </ul>
 *
 * <p>This configuration enables Swagger UI to support authentication
 * by allowing users to enter a JWT token and access secured APIs.</p>
 *
 * <p>Example Usage in Swagger UI:</p>
 * <pre>
 * Click "Authorize" →
 * Enter: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 * </pre>
 *
 * @author Abhishek Tadiwal
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates and configures the OpenAPI bean.
     *
     * <p>This method defines:</p>
     * <ul>
     *     <li>API information (title, version, description, contact)</li>
     *     <li>Global security requirement using BearerAuth</li>
     *     <li>Security scheme for JWT-based authentication</li>
     * </ul>
     *
     * @return configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Loan API")
                        .version("1.0")
                        .description("REST APIs for Loan Management System (Users, Loans, EMI, Payments, Documents)")
                        .contact(new Contact()
                                .name("Abhishek Tadiwal")
                                .email("pankajtariyal27@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
