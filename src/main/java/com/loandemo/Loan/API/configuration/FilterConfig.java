package com.loandemo.Loan.API.configuration;

import com.loandemo.Loan.API.filter.JwtAuthenticationFilter;
import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.service.UserDetailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security.
 *
 * <p>This class defines authentication, authorization, password encoding,
 * and JWT-based security filter configuration for the application.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Configure {@link AuthenticationProvider} using {@link DaoAuthenticationProvider}</li>
 *     <li>Provide {@link AuthenticationManager} bean</li>
 *     <li>Define {@link SecurityFilterChain} for securing HTTP endpoints</li>
 *     <li>Configure stateless session management (JWT-based authentication)</li>
 *     <li>Register custom {@link JwtAuthenticationFilter}</li>
 *     <li>Define {@link PasswordEncoder} using BCrypt</li>
 * </ul>
 *
 * <p>Security Features:</p>
 * <ul>
 *     <li>Disables CSRF and CORS (suitable for stateless REST APIs)</li>
 *     <li>Uses JWT authentication instead of session-based authentication</li>
 *     <li>Allows public access to Swagger, authentication, and health endpoints</li>
 *     <li>Restricts admin endpoints to users with role <b>ADMIN</b></li>
 * </ul>
 *
 * <p>Example:</p>
 * <pre>
 * Public endpoints:
 *   /api/v1/auth/**
 *   /api/v1/swagger-ui/**
 *
 * Protected endpoints:
 *   /api/v1/admin  → requires ROLE_ADMIN
 *   others         → require authentication
 * </pre>
 *
 * @author Abhishek Tadiwal
 */
@Configuration
public class FilterConfig {

    /**
     * Custom implementation of UserDetailsService used for authentication.
     */
    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    /**
     * Utility class for JWT operations such as token validation and extraction.
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Configures the authentication provider using DAO-based authentication.
     *
     * <p>This provider uses {@link UserDetailServiceImp} to load user details
     * and {@link BCryptPasswordEncoder} for password matching.</p>
     *
     * @return configured {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceImp);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Provides the {@link AuthenticationManager} bean used for authentication.
     *
     * <p>This manager delegates authentication requests to configured
     * authentication providers.</p>
     *
     * @param configuration Spring authentication configuration
     * @return {@link AuthenticationManager} instance
     * @throws Exception if authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p>Key configurations:</p>
     * <ul>
     *     <li>Disables CSRF and CORS</li>
     *     <li>Sets session management to STATELESS (JWT-based)</li>
     *     <li>Defines public and protected endpoints</li>
     *     <li>Registers {@link JwtAuthenticationFilter}</li>
     * </ul>
     *
     * @param security the {@link HttpSecurity} object to configure
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Public Swagger & utility endpoints
                    auth.antMatchers(
                            "/api/v1/swagger-ui.html",
                            "/swagger-ui.html",
                            "/api/v1/swagger-ui/**",
                            "/swagger-ui/**",
                            "/api/v1/v3/api-docs/**",
                            "/api/v1/swagger-resources/**",
                            "/api/v1/webjars/**",
                            "/api/v1/actuator/**",
                            "/api/v1/index/**"
                    ).permitAll();

                    // Public authentication endpoints
                    auth.antMatchers("/api/v1/auth/**", "/api/v1/hello/**","/api/v1/email/**").permitAll();

                    // Admin-only endpoint
                    auth.antMatchers("/api/v1/admin").hasRole("ADMIN");

                    // All other endpoints require authentication
                    auth.anyRequest().authenticated();
                });

        // Add JWT filter before default authentication filter
        security.addFilterBefore(
                new JwtAuthenticationFilter(jwtUtils, userDetailServiceImp),
                UsernamePasswordAuthenticationFilter.class
        );

        return security.build();
    }

    /**
     * Defines the password encoder bean.
     *
     * <p>Uses {@link BCryptPasswordEncoder} with strength 12
     * for secure password hashing.</p>
     *
     * @return {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
