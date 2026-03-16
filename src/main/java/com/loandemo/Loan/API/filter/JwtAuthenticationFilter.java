package com.loandemo.Loan.API.filter;

import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.service.UserDetailServiceImp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailServiceImp userDetailsServiceImp;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailServiceImp userDetailsService){
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImp = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        // Skip Swagger endpoints
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
        if(header!=null && header.startsWith("Bearer")){
            token = header.substring(7);
            username = jwtUtils.extractUsername(token);
        }
        if(username!=null){
            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username);
            if(jwtUtils.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        response.setHeader("user",username);
        filterChain.doFilter(request,response);
    }
}
