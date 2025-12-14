package com.afrione.africoinservice.infrastructure.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Duration;


@Slf4j
@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${DATABASE_NAME}")
    private String databaseName;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${database.pool-name:AfriCoinADBPool}")
    private String databasePoolName;

    @Value("${database.pool-size:8}")
    private int databasePoolSize;

    @Bean
    @Primary
    public DataSource hikariDataSource() throws SQLException {

        String dbUrl = databaseUrl+"/"+databaseName;
        System.out.println("database url: " + dbUrl);
        HikariConfig config = new HikariConfig();
        config.setUsername(databaseUsername);
        config.setPassword(databasePassword);
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(dbUrl);
        config.setMaximumPoolSize(databasePoolSize); // DEFAULT IS 10
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(300000);
        config.setMaxLifetime(300000);
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setPoolName(databasePoolName);
        return new HikariDataSource(config);
    }

    @Bean
    public RestClient restClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(55));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .requestInterceptor(new RequestResponseLoggingInterceptor())
                .build();
    }

    private static class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            logRequest(httpRequest, bytes);
            ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
            logResponse(response);
            return response;
        }
        private void logRequest(HttpRequest request, byte[] body) throws IOException {
            String requestBody = new String(body, StandardCharsets.UTF_8);
            String url = request.getURI().toString();
            /*if (log.isDebugEnabled()) {
                log.debug("Method/URI     : {} / {}", request.getMethod(), url);
                log.debug("Request body: {}", requestBody);
            }*/
            log.info("Method/URI     : {} / {}", request.getMethod(), url);
            log.info("Request body: {}", requestBody);
        }
        private void logResponse(ClientHttpResponse response) throws IOException {
            log.info("Status code/Text : {} / {}", response.getStatusCode() , response.getStatusText());
            if (log.isDebugEnabled()) {
                //String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()); //{"access_token"
                //log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            }
        }
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }
}
