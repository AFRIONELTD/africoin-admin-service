package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Generic helper for GET requests that return an ApiResponseJSON wrapper.
 * Reduces duplicated code across merchant/customer read use cases.
 */
@Slf4j
public final class RetrievalHelper {

    private RetrievalHelper() {}

    public static <T> T get(RestClientService restClientService,
                            ObjectMapper objectMapper,
                            String url,
                            Map<String, String> headers,
                            TypeReference<ApiResponseJSON<T>> typeRef) {
        try {
            RestClientResponse response = restClientService.getRequest(url, headers);

            if (response == null || response.getStatusCode() == null || !response.getStatusCode().is2xxSuccessful()) {
                int status = response != null && response.getStatusCode() != null ? response.getStatusCode().value() : -1;
                log.warn("Failed to GET {}. Status: {}", url, status);
                String body = response != null ? response.getResponseBody() : null;
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve data");
                }
            }

            String responseBody = response.getResponseBody();
            ApiResponseJSON<T> apiResponseJSON = objectMapper.readValue(responseBody, typeRef);
            return apiResponseJSON.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving data from {}", url, e);
            throw new BadRequestException("Error retrieving data: " + e.getMessage());
        }
    }
}

