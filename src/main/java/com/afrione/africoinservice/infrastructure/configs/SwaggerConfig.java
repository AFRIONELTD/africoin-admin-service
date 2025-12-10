package com.afrione.africoinservice.infrastructure.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;


@Profile({"dev", "test", "sandbox"})
@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Autowired
    Optional<GitProperties> gitInfo;

    @Bean
    public GroupedOpenApi clientApi () {
        return GroupedOpenApi.builder()
                .group("APP-SERVICE")
                .packagesToScan("com.afrione.africoinservice.infrastructure.web.controllers")
                .build();
    }


    @Bean
    public OpenAPI afriCoinOpenAPI() {
        String version = "1.0";
        if(gitInfo.isPresent()) {
            version = String.format("%s - %s ",  gitInfo.get().getShortCommitId(), gitInfo.get().getBranch());
        }
        return new OpenAPI()
                .info(new Info().title("AfriCoin Admin Service API")
                        .description("Admin Client Api Documentation")
                        .version(version)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }
}
