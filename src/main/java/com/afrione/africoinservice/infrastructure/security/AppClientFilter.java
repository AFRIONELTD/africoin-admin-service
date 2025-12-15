package com.afrione.africoinservice.infrastructure.security;

import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
public class AppClientFilter extends OncePerRequestFilter {

    private final RequestMatcher filterPaths = new AntPathRequestMatcher("/api/v*/*");

    private final ApplicationProperty applicationProperty;

    public AppClientFilter(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
        hResponse.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        hResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD, PATCH, PUT");
        hResponse.setHeader("Access-Control-Max-Age", "3600");
        hResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With, remember-me, x-timezone, client-key, x-request-client-key");

        String url = request.getRequestURI();

        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return filterPaths.matches(request);
    }
}
