package com.afrione.africoinservice.utils;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.exceptions.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Created by felixadewale on
 * 12/01/2026
 */
public class APIRequestHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static void handleErrorResponse(RestClientResponse response) throws JsonProcessingException {
        ApiResponseJSON<?> apiResp = objectMapper.readValue(response.getResponseBody(), new TypeReference<ApiResponseJSON<?>>() {
        });
        String message = apiResp != null && apiResp.getMessage() != null ? apiResp.getMessage() : "Error Making remote call";
        HttpStatus httpStatus = response.getStatusCode() != null ? HttpStatus.resolve(response.getStatusCode().value()) : null;
        if (httpStatus == null) httpStatus = HttpStatus.BAD_GATEWAY;
        throw new APIRequestException(httpStatus, message);
    }

    public static String extractResponseMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "Successful";
        }
        try {
            ApiResponseJSON<String>  response = objectMapper.readValue(responseBody, new TypeReference<ApiResponseJSON<String>>() {
            });
            return response != null && response.getData() != null ? response.getData(): "Successful";
        } catch (Exception e) {

            return "Successful";
        }
    }
}
