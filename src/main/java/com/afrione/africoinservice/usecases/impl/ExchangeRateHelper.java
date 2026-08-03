package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.request.B2cExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Helper that centralizes GET retrieval and POST update logic for exchange rates.
 * Exposes three methods: `updaterate`, `stat`, and `retrieve`.
 */
@Slf4j
public final class ExchangeRateHelper {

    private ExchangeRateHelper() {}

    private static <T> T getGeneric(RestClientService restClientService,
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


    public static void updateRate(RestClientService restClientService,
                                  ObjectMapper objectMapper,
                                  String url,
                                  B2cExchangeRateUpdateRequest requests,
                                  Map<String, String> headers) {
        try {
            if (requests == null ) {
                throw new BadRequestException("At least one exchange rate update is required");
            }

            String requestPayload = objectMapper.writeValueAsString(requests);

            log.info("Sending exchange rate update request for currency {}", url);

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (response == null || response.getStatusCode() == null || !response.getStatusCode().is2xxSuccessful()) {
                int statusCode = response != null && response.getStatusCode() != null ? response.getStatusCode().value() : -1;
                log.warn("Failed to update exchange rates. Status: {}", statusCode);
                String body = response != null ? response.getResponseBody() : null;
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to update exchange rates");
                }
            }

            log.info("Successfully updated exchange rates");
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating exchange rates", e);
            throw new BadRequestException("Error updating exchange rates: " + e.getMessage());
        }
    }

    public static void updateRate(RestClientService restClientService,
                                  ObjectMapper objectMapper,
                                  String url,
                                  List<ExchangeRateUpdateRequest> requests,
                                  Map<String, String> headers) {
        try {
            if (requests == null || requests.isEmpty()) {
                throw new BadRequestException("At least one exchange rate update is required");
            }

            String requestPayload = objectMapper.writeValueAsString(requests);

            log.info("Sending exchange rate update request for {} currency pairs to {}", requests.size(), url);

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (response == null || response.getStatusCode() == null || !response.getStatusCode().is2xxSuccessful()) {
                int statusCode = response != null && response.getStatusCode() != null ? response.getStatusCode().value() : -1;
                log.warn("Failed to update exchange rates. Status: {}", statusCode);
                String body = response != null ? response.getResponseBody() : null;
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to update exchange rates");
                }
            }

            log.info("Successfully updated {} exchange rates", requests.size());
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating exchange rates", e);
            throw new BadRequestException("Error updating exchange rates: " + e.getMessage());
        }
    }

    public static <T> T stat(RestClientService restClientService,
                             ObjectMapper objectMapper,
                             String url,
                             Map<String, String> headers,
                             TypeReference<ApiResponseJSON<T>> typeRef) {
        return getGeneric(restClientService, objectMapper, url, headers, typeRef);
    }

    public static <T> T retrieve(RestClientService restClientService,
                                 ObjectMapper objectMapper,
                                 String url,
                                 Map<String, String> headers,
                                 TypeReference<ApiResponseJSON<T>> typeRef) {
        return getGeneric(restClientService, objectMapper, url, headers, typeRef);
    }
}
