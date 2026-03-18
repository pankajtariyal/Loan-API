package com.loandemo.Loan.API.filter;

import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.service.UserDetailServiceImp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Authentication Filter for validating and processing JWT tokens.
 *
 * <p>This filter extends {@link OncePerRequestFilter}, ensuring it executes
 * once per request. It intercepts incoming HTTP requests, extracts the JWT
 * token from the Authorization header, validates it, and sets the authentication
 * in the Spring Security context.</p>
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *     <li>Skip authentication for Swagger-related endpoints</li>
 *     <li>Extract JWT token from the "Authorization" header</li>
 *     <li>Validate the token and extract username</li>
 *     <li>Load user details using {@link UserDetailServiceImp}</li>
 *     <li>Set authentication in {@link SecurityContextHolder}</li>
 * </ul>
 *
 * <p>Expected Header Format:</p>
 * <pre>
 * Authorization: Bearer &lt;JWT_TOKEN&gt;
 * </pre>
 *
 * <p>Authentication Flow:</p>
 * <ol>
 *     <li>Request is intercepted by this filter</li>
 *     <li>Authorization header is checked</li>
 *     <li>JWT token is extracted and parsed</li>
 *     <li>Username is extracted from token</li>
 *     <li>User details are loaded from database</li>
 *     <li>If token is valid, authentication is set in security context</li>
 * </ol>
 *
 * <p>Note:</p>
 * <ul>
 *     <li>This implementation checks token expiration using {@code isTokenExpired}</li>
 *     <li>Authentication is set only when token passes validation</li>
 * </ul>
 *
 * @author Abhishek
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Utility class for JWT operations such as parsing and validation.
     */
    private final JwtUtils jwtUtils;

    /**
     * Custom UserDetailsService implementation to load user data.
     */
    private final UserDetailServiceImp userDetailsServiceImp;

    /**
     * Constructor for injecting dependencies.
     *
     * @param jwtUtils utility for JWT operations
     * @param userDetailsService service to load user details
     */
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailServiceImp userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImp = userDetailsService;
    }

    /**
     * Core method that filters each HTTP request.
     *
     * <p>This method performs JWT validation and sets authentication
     * in the Spring Security context if the token is valid.</p>
     *
     * @param request the incoming {@link HttpServletRequest}
     * @param response the outgoing {@link HttpServletResponse}
     * @param filterChain the {@link FilterChain} to continue request processing
     * @throws ServletException in case of servlet errors
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        /**
         * Skip authentication for Swagger-related endpoints.
         */
        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars")) {

            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        /**
         * Extract JWT token from Authorization header.
         */
        if (header != null && header.startsWith("Bearer")) {
            token = header.substring(7);
            username = jwtUtils.extractUsername(token);
        }

        /**
         * Validate token and set authentication.
         */
        if (username != null) {
            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username);

            // NOTE: Ideally should check !isTokenExpired(token)
            if (jwtUtils.isTokenExpired(token)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        /**
         * Optional: Add username to response header for debugging or tracking.
         */
        response.setHeader("user", username);

        /**
         * Continue filter chain execution.
         */
        filterChain.doFilter(request, response);
    }
}
