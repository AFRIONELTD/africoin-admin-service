package com.afrione.africoinservice.infrastructure.serviceimpl;

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


@Service
@RequiredArgsConstructor
@Slf4j
public class RestClientServiceImpl implements RestClientService {

    private final RestClient restClient;
    private final ApiRequestLogEntityDao apiRequestLogEntityDao;

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload) {
        Map<String, String> emptyMap = new HashMap<>();
        return postRequest(serviceUrl, requestPayload, emptyMap);
    }

    @Override
    public RestClientResponse postRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap) {
        LocalDateTime now = LocalDateTime.now();
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                logEntity = apiRequestLogEntityDao.createLog(serviceUrl, requestPayload);
                if (logEntity != null) logEntity.setHttpMethod("POST");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", serviceUrl, e);
            }

            ResponseEntity<String> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(String.class);

            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseBody(StringUtils.defaultString(result.getBody()))
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.MILLIS))
                    .build();

            // update log with response
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", serviceUrl, e);
            }

            return response;
        }catch (HttpClientErrorException httpClientErrorException) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (client error) for url {}", serviceUrl, e);
            }
            return response;
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (exception) for url {}", serviceUrl, e);
            }
            return response;
        }
    }

    @Override
    public <T, K> RestClientResponse postRequest(String serviceUrl, T requestPayload, Map<String, String> headerMap,
                                                 Class<K> responseType) {
        LocalDateTime now = LocalDateTime.now();
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                String payload = requestPayload == null ? null : requestPayload.toString();
                logEntity = apiRequestLogEntityDao.createLog(serviceUrl, payload);
                if (logEntity != null) logEntity.setHttpMethod("POST");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", serviceUrl, e);
            }

            ResponseEntity<K> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(responseType);
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseObject(result.getBody())
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();

            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", serviceUrl, e);
            }

            return response;
        }catch (HttpClientErrorException httpClientErrorException) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (client error) for url {}", serviceUrl, e);
            }
            return response;
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (exception) for url {}", serviceUrl, e);
            }
            return response;
        }
    }


    @Override
    public <T,K> RestClientResponse postFormRequest(String serviceUrl, T requestPayload, Class<K> responseType) {
        LocalDateTime now = LocalDateTime.now();
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                String payload = requestPayload == null ? null : requestPayload.toString();
                logEntity = apiRequestLogEntityDao.createLog(serviceUrl, payload);
                if (logEntity != null) logEntity.setHttpMethod("POST");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", serviceUrl, e);
            }

            ResponseEntity<K> result = restClient.post()
                    .uri(serviceUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestPayload)
                    .retrieve()
                    .toEntity(responseType);
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(result.getStatusCode())
                    .responseObject(result.getBody())
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();

            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", serviceUrl, e);
            }

            return response;
        }catch (HttpClientErrorException httpClientErrorException) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(httpClientErrorException.getStatusCode())
                    .responseBody(httpClientErrorException.getResponseBodyAsString())
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (client error) for url {}", serviceUrl, e);
            }
            return response;
        }catch (Exception exception) {
            System.out.println(exception.getClass());
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (exception) for url {}", serviceUrl, e);
            }
            return response;
        }
    }


    @Override
    public RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap) {
        LocalDateTime now = LocalDateTime.now();
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                logEntity = apiRequestLogEntityDao.createLog(serviceUrl, null);
                if (logEntity != null) logEntity.setHttpMethod("GET");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", serviceUrl, e);
            }

            RestClientResponse response = restClient.get()
                    .uri(serviceUrl)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .exchange((request, response2) -> RestClientResponse.builder()
                            .responseBody(response2.bodyTo(String.class))
                            .statusCode(response2.getStatusCode())
                            .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                            .build());

            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", serviceUrl, e);
            }

            return response;
        } catch (Exception e) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception ex) {
                log.warn("Failed to update API request log (exception) for url {}", serviceUrl, ex);
            }
            return response;
        }
    }

    @Override
    public <T> RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap, Class<T> responseType, Map<String, Object> params ) {
        LocalDateTime now = LocalDateTime.now();

        String url = buildUrlSpring(serviceUrl, params);
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                logEntity = apiRequestLogEntityDao.createLog(url, null);
                if (logEntity != null) logEntity.setHttpMethod("GET");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", url, e);
            }

            RestClientResponse response = restClient.get()
                    .uri(url)
                    .headers(httpHeaders -> headerMap.forEach(httpHeaders::set))
                    .exchange((request, response2) -> RestClientResponse.builder()
                            .responseObject(response2.bodyTo(responseType))
                            .statusCode(response2.getStatusCode())
                            .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                            .build());

            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", url, e);
            }

            return response;
        } catch (Exception e) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception ex) {
                log.warn("Failed to update API request log (exception) for url {}", url, ex);
            }
            return response;
        }
    }

    @Override
    public RestClientResponse getRequest(String serviceUrl) {
        LocalDateTime now = LocalDateTime.now();
        ApiRequestLogEntity logEntity = null;
        try {
            try {
                logEntity = apiRequestLogEntityDao.createLog(serviceUrl, null);
                if (logEntity != null) logEntity.setHttpMethod("GET");
            } catch (Exception e) {
                log.warn("Failed to create API request log for url {}", serviceUrl, e);
            }

            RestClientResponse response = restClient.get()
                    .uri(serviceUrl)
                    .exchange((request, response2) -> RestClientResponse.builder()
                            .responseBody(response2.bodyTo(String.class))
                            .statusCode(response2.getStatusCode())
                            .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                            .build());

            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception e) {
                log.warn("Failed to update API request log (success) for url {}", serviceUrl, e);
            }

            return response;
        } catch (Exception e) {
            RestClientResponse response = RestClientResponse.builder()
                    .statusCode(HttpStatusCode.valueOf(HttpStatus.GATEWAY_TIMEOUT.value()))
                    .responseBody("")
                    .timeTakenInMs(now.until(LocalDateTime.now(), ChronoUnit.SECONDS))
                    .build();
            try {
                if (logEntity != null) apiRequestLogEntityDao.updateLog(logEntity, response);
            } catch (Exception ex) {
                log.warn("Failed to update API request log (exception) for url {}", serviceUrl, ex);
            }
            return response;
        }
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
