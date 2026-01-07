package com.afrione.africoinservice.infrastructure.security;


import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Configuration
public class UserAuthenticationConfig {

    private final JWTService jwtService;
    private final AppUserEntityDao appUserEntityDao;
    private final ApplicationProperty applicationProperty;

    public UserAuthenticationConfig(JWTService jwtService, AppUserEntityDao appUserEntityDao, ApplicationProperty applicationProperty) {
        this.jwtService = jwtService;
        this.appUserEntityDao = appUserEntityDao;
        this.applicationProperty = applicationProperty;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return token -> {
            return findUserByToken(token).orElseThrow(() -> new UsernameNotFoundException("Invalid authorization token."));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        String salt = "jvfhsjdhk12h3kkslkcM@nsU";
        return new BCryptPasswordEncoder(10, new SecureRandom(salt.getBytes()));
    }

    private Optional<AuthenticatedUser> findUserByToken(String token) {

        Map<String, String> attributes = jwtService.verify(applicationProperty.getClientTokenSecretKey(), token);

        if (attributes.isEmpty()) {
            return Optional.empty();
        }
        if (!attributes.containsKey("userId")) {
            return Optional.empty();
        }

        final long userId = Long.parseLong(attributes.get("userId"));
        final String authKey = attributes.get("authKey");
        String privilegeCodes = attributes.getOrDefault("privilegeCodes", "");
        String tokenType = attributes.getOrDefault(AppConstant.TOKEN_TYPE, "");
        String accountType = attributes.getOrDefault("accountType", "");

        if (AppConstant.TOKEN_TYPE_REFRESH.equalsIgnoreCase(tokenType)) {
            System.out.println("refresh token: " + token);
            return Optional.empty();
        }
        if (accountType.equalsIgnoreCase(AppConstant.ACCOUNT_TYPE_ADMIN)) {
            Optional<AppUserEntity> appUserOptional = appUserEntityDao.findById(userId);
            if (appUserOptional.isEmpty()) {
                return Optional.empty();
            }
            AppUserEntity appUser = appUserOptional.get();
            if (StringUtils.isNotEmpty(appUser.getAuthenticationKey()) && !appUser.getAuthenticationKey().equalsIgnoreCase(authKey)) {
                System.out.println("auth key mismatch - " + authKey + " " + appUser.getAuthenticationKey());
                return Optional.empty();
            }
            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setAccountId(userId);
            authenticatedUser.setUserId(userId);
            authenticatedUser.setUsername(attributes.get("email"));

            authenticatedUser.addAuthority(accountType);
            return Optional.of(authenticatedUser);

        }
        return Optional.empty();
    }
}
