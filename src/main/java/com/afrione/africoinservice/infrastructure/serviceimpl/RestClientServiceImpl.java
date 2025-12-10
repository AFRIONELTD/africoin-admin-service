package com.afrione.africoinservice.infrastructure.serviceimpl;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.RestClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class RestClientServiceImpl implements RestClientService {

    private final RestClient restClient;

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload) {
        Map<String, String> emptyMap = new HashMap<>();
        return postRequest(serviceUrl, requestPayload, emptyMap);
    }

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap) {
        LocalDateTime now = LocalDateTime.now();
        try {
            ResponseEntity<String> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(String.class);
            return RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseBody(StringUtils.defaultString(result.getBody()))
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (HttpClientErrorException httpClientErrorException) {
            return RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            return RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }
    }

    @Override
    public <T, K> RestClientResponse postRequest(String serviceUrl, T requestPayload, Map<String, String> headerMap,
                                                 Class<K> responseType) {
        LocalDateTime now = LocalDateTime.now();
        try {
            ResponseEntity<K> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(responseType);
            return RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseObject(result.getBody())
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (HttpClientErrorException httpClientErrorException) {
            return RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            return RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }
    }


    @Override
    public <T,K> RestClientResponse postFormRequest(String serviceUrl, T requestPayload, Class<K> responseType) {
        LocalDateTime now = LocalDateTime.now();
        try {
            ResponseEntity<K> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(responseType);
            return RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseObject(result.getBody())
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (HttpClientErrorException httpClientErrorException) {
            return RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            return RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
        }
    }


    @Override
    public RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap) {
        LocalDateTime now = LocalDateTime.now();
        return restClient.get()
                .uri(serviceUrl)
                .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                .exchange((request, response) -> RestClientResponse.builder()
                        .responseBody(response.bodyTo(String.class))
                        .statusCode(response.getStatusCode())
                        .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                        .build());
    }

    @Override
    public <T> RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap, Class<T> responseType, Map<String, Object> params ) {
        LocalDateTime now = LocalDateTime.now();

        String url = buildUrlSpring(serviceUrl, params);

        return restClient.get()
                .uri(serviceUrl)
                .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                .exchange((request, response) -> RestClientResponse.builder()
                        .responseObject(response.bodyTo(responseType))
                        .statusCode(response.getStatusCode())
                        .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                        .build());
    }

    @Override
    public RestClientResponse getRequest(String serviceUrl) {
        LocalDateTime now = LocalDateTime.now();
        return restClient.get()
                .uri(serviceUrl)
                .exchange((request, response) -> RestClientResponse.builder()
                        .responseBody(response.bodyTo(String.class))
                        .statusCode(response.getStatusCode())
                        .timeTakenInSeconds(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                        .build());
    }


    private String buildUrlSpring(String baseUrl, Map<String, Object> params) {
        if(params == null || params.isEmpty()) {
            return baseUrl;
        }

        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(baseUrl);
        params.forEach((k, v) -> {
            if (v == null) return;
            if (v instanceof Collection<?> coll) {
                coll.forEach(item -> { if (item != null) b.queryParam(k, item); });
            } else if (v.getClass().isArray()) {
                int len = java.lang.reflect.Array.getLength(v);
                for (int i = 0; i < len; i++) {
                    Object item = java.lang.reflect.Array.get(v, i);
                    if (item != null) b.queryParam(k, item);
                }
            } else {
                b.queryParam(k, v);
            }
        });
        return b.build(true).toUriString(); // true = encode
    }
}
