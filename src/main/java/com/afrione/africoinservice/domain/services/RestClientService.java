package com.afrione.africoinservice.domain.services;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * Created by jnya on
 * Mon, 01 Sept, 2025
 */
public interface RestClientService {
    RestClientResponse postRequest(String serviceUrl, String requestPayload);
    RestClientResponse putRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap);
    RestClientResponse postRequest(String serviceUrl, String requestPayload, Map<String, String> headerMap);
    RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap);
    RestClientResponse getRequest(String serviceUrl);
    <T, K> RestClientResponse postRequest(String serviceUrl, T requestPayload, Map<String, String> headerMap, Class<K> responseType);
    <T, K>  RestClientResponse postFormRequest(String serviceUrl, T requestPayload, Class<K> responseType);
    <T> RestClientResponse getRequest(String serviceUrl, Map<String, String> headerMap, Class<T> responseType,  Map<String, Object> params);
}
