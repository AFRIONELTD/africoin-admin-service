package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.ApiRequestLogEntityDao;
import com.afrione.africoinservice.domain.entities.ApiRequestLogEntity;
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
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestClientServiceImpl implements RestClientService {

    private final RestClient restClient;
    private final ApiRequestLogEntityDao apiRequestLogEntityDao;

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload) {
        return postRequest(serviceUrl, requestPayload, new HashMap<>());
    }

    @Override
    public RestClientResponse putRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap) {
        return executeRequest(serviceUrl, requestPayload, "PUT", () ->
                restClient.put()
                        .uri(serviceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(h -> headerMap.forEach(h::set))
                        .body(requestPayload)
                        .retrieve()
                        .toEntity(String.class));
    }

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap) {
        return executeRequest(serviceUrl, requestPayload, "POST", () ->
                restClient.post()
                        .uri(serviceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(h -> headerMap.forEach(h::set))
                        .body(requestPayload)
                        .retrieve()
                        .toEntity(String.class));
    }

    @Override
    public <T, K> RestClientResponse postRequest(String serviceUrl, T requestPayload,
                                                 Map<String, String> headerMap, Class<K> responseType) {
        String payload = requestPayload == null ? null : requestPayload.toString();
        return executeRequestWithObject(serviceUrl, payload, "POST", () ->
                restClient.post()
                        .uri(serviceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(h -> headerMap.forEach(h::set))
                        .body(requestPayload)
                        .retrieve()
                        .toEntity(responseType));
    }

    @Override
    public <T, K> RestClientResponse postFormRequest(String serviceUrl, T requestPayload, Class<K> responseType) {
        String payload = requestPayload == null ? null : requestPayload.toString();
        return executeRequestWithObject(serviceUrl, payload, "POST", () ->
                restClient.post()
                        .uri(serviceUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(requestPayload)
                        .retrieve()
                        .toEntity(responseType));
    }

    @Override
    public RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap) {
        return executeGetRequest(serviceUrl, headerMap, null, null);
    }

    @Override
    public <T> RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap,
                                             Class<T> responseType, Map<String, Object> params) {
        return executeGetRequest(buildUrlSpring(serviceUrl, params), headerMap, responseType, null);
    }

    @Override
    public RestClientResponse getRequest(String serviceUrl) {
        return executeGetRequest(serviceUrl, new HashMap<>(), null, null);
    }

    private RestClientResponse executeRequest(String serviceUrl, String requestPayload,
                                              String httpMethod, Supplier<ResponseEntity<String>> requestSupplier) {
        LocalDateTime start = LocalDateTime.now();
        ApiRequestLogEntity logEntity = createLog(serviceUrl, requestPayload, httpMethod);

        try {
            ResponseEntity<String> result = requestSupplier.get();
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseBody(StringUtils.defaultString(result.getBody()))
                    .timeTakenInMs(ChronoUnit.MILLIS.between(start, LocalDateTime.now()))
                    .build();
            updateLog(logEntity, response, serviceUrl);
            return response;
        } catch (HttpClientErrorException e) {
            return handleHttpClientError(e, start, logEntity, serviceUrl);
        } catch (Exception e) {
            return handleGenericError(e, start, logEntity, serviceUrl);
        }
    }

    private <K> RestClientResponse executeRequestWithObject(String serviceUrl, String requestPayload,
                                                            String httpMethod, Supplier<ResponseEntity<K>> requestSupplier) {
        LocalDateTime start = LocalDateTime.now();
        ApiRequestLogEntity logEntity = createLog(serviceUrl, requestPayload, httpMethod);

        try {
            ResponseEntity<K> result = requestSupplier.get();
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseObject(result.getBody())
                    .timeTakenInMs(ChronoUnit.MILLIS.between(start, LocalDateTime.now()))
                    .build();
            updateLog(logEntity, response, serviceUrl);
            return response;
        } catch (HttpClientErrorException e) {
            return handleHttpClientError(e, start, logEntity, serviceUrl);
        } catch (Exception e) {
            return handleGenericError(e, start, logEntity, serviceUrl);
        }
    }

    private <T> RestClientResponse executeGetRequest(String serviceUrl, Map<String, String> headerMap,
                                                     Class<T> responseType, Map<String, Object> params) {
        LocalDateTime start = LocalDateTime.now();
        ApiRequestLogEntity logEntity = createLog(serviceUrl, null, "GET");

        try {
            RestClientResponse response = restClient.get()
                    .uri(serviceUrl)
                    .headers(h -> headerMap.forEach(h::set))
                    .exchange((req, res) -> RestClientResponse.builder()
                            .responseBody(responseType == null ? res.bodyTo(String.class) : null)
                            .responseObject(responseType != null ? res.bodyTo(responseType) : null)
                            .statusCode(res.getStatusCode())
                            .timeTakenInMs(ChronoUnit.MILLIS.between(start, LocalDateTime.now()))
                            .build());
            updateLog(logEntity, response, serviceUrl);
            return response;
        } catch (Exception e) {
            return handleGenericError(e, start, logEntity, serviceUrl);
        }
    }

    private ApiRequestLogEntity createLog(String serviceUrl, String requestPayload, String httpMethod) {
        try {
            ApiRequestLogEntity logEntity = apiRequestLogEntityDao.createLog(serviceUrl, requestPayload);
            if (logEntity != null) {
                logEntity.setHttpMethod(httpMethod);
            }
            return logEntity;
        } catch (Exception e) {
            log.warn("Failed to create API request log for url {}", serviceUrl, e);
            return null;
        }
    }

    private void updateLog(ApiRequestLogEntity logEntity, RestClientResponse response, String serviceUrl) {
        try {
            if (logEntity != null) {
                apiRequestLogEntityDao.updateLog(logEntity, response);
            }
        } catch (Exception e) {
            log.warn("Failed to update API request log for url {}", serviceUrl, e);
        }
    }

    private RestClientResponse handleHttpClientError(HttpClientErrorException e, LocalDateTime start,
                                                     ApiRequestLogEntity logEntity, String serviceUrl) {
        RestClientResponse response = RestClientResponse.builder()
                .statusCode(e.getStatusCode())
                .responseBody(e.getResponseBodyAsString())
                .timeTakenInMs(ChronoUnit.MILLIS.between(start, LocalDateTime.now()))
                .build();
        updateLog(logEntity, response, serviceUrl);
        return response;
    }

    private RestClientResponse handleGenericError(Exception e, LocalDateTime start,
                                                  ApiRequestLogEntity logEntity, String serviceUrl) {
        log.error("Exception occurred for url {}: {}", serviceUrl, e.getClass().getName());
        RestClientResponse response = RestClientResponse.builder()
                .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                .responseBody("")
                .timeTakenInMs(ChronoUnit.MILLIS.between(start, LocalDateTime.now()))
                .build();
        updateLog(logEntity, response, serviceUrl);
        return response;
    }

    private String buildUrlSpring(String baseUrl, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        params.forEach((key, value) -> {
            if (value == null) return;
            if (value instanceof Collection<?> coll) {
                coll.forEach(item -> {
                    if (item != null) builder.queryParam(key, item);
                });
            } else if (value.getClass().isArray()) {
                int len = java.lang.reflect.Array.getLength(value);
                for (int i = 0; i < len; i++) {
                    Object item = java.lang.reflect.Array.get(value, i);
                    if (item != null) builder.queryParam(key, item);
                }
            } else {
                builder.queryParam(key, value);
            }
        });
        return builder.build(true).toUriString();
    }
}