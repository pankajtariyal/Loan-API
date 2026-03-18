package com.loandemo.Loan.API.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to apply a global API prefix to all REST controllers.
 *
 * <p>This class implements {@link WebMvcConfigurer} to customize Spring MVC configuration.
 * It automatically adds the prefix <b>"api/v1"</b> to all request mappings
 * of classes annotated with {@link RestController}.</p>
 *
 * <p>Example:</p>
 * <pre>
 * Before applying this configuration:
 *   @GetMapping("/user")
 *   URL → /users
 *
 * After applying this configuration:
 *   URL → /api/v1/users
 * </pre>
 *
 * <p>This helps in versioning APIs and maintaining backward compatibility.</p>
 *
 * @author Abhishek Tadiwal
 */
@Configuration
public class ApiPrefixConfig implements WebMvcConfigurer {

    /**
     * Configures path matching by adding a prefix to all REST controller endpoints.
     *
     * <p>The prefix "api/v1" will be applied only to classes annotated with
     * {@link RestController}. Other controllers (e.g., {@code @Controller})
     * will not be affected.</p>
     *
     * @param configurer the {@link PathMatchConfigurer} used to configure path matching
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("api/v1",
                c -> c.isAnnotationPresent(RestController.class));
    }
}
