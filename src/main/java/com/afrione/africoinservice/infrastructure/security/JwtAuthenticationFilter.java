package com.afrione.africoinservice.infrastructure.security;

import com.afrione.africoinservice.infrastructure.configs.SecurityConfig;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            Optional<String> paramOptional = ofNullable(request.getHeader(AUTHORIZATION));

            if(paramOptional.isEmpty() || !paramOptional.get().toLowerCase().startsWith("bearer")) {
                System.out.println(paramOptional.get());
                throw new BadCredentialsException("Bad Authorization Token format.");
            }
            final String token = paramOptional.get().substring(7);

            UserDetails authenticatedUser = userDetailsService.loadUserByUsername(token);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        }catch (Exception ex) {
            logger.error("JWT Authentication failed:", ex);
            String message = ex.getMessage();
            System.out.println("JWT Authentication Failed: " + message);
            ApiResponseJSON<String> apiResponse = new ApiResponseJSON<>(message);
            response.setHeader("Content-Type", "application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getOutputStream().write(new ObjectMapper().writeValueAsString(apiResponse).getBytes());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return SecurityConfig.PUBLIC_URLS.matches(request);
    }
}
