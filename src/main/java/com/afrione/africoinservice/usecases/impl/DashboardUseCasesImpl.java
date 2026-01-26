package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.DashboardUseCases;
import com.afrione.africoinservice.usecases.data.response.dashboard.CrossBorderSummaryResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Created by felixadewale on
 * 26/01/2026
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DashboardUseCasesImpl implements DashboardUseCases {

    private final RestClientService restClientService;
    private final ApplicationProperty applicationProperty;
    private final ObjectMapper objectMapper;


    @Override
    public CrossBorderSummaryResponse getCrossBoarderAnalytics(AuthenticatedUser authenticatedUser, LocalDate startDate, LocalDate endDate) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(getApplicationProperty().customerServiceUrl())
                    .append("/api/v2/admin/dashboard/crossborder");

            boolean hasParam = false;

            if (startDate != null) {
                url.append(hasParam ? "&" : "?")
                        .append("startDate=")
                        .append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
                hasParam = true;
            }

            if (endDate != null) {
                url.append(hasParam ? "&" : "?")
                        .append("endDate=")
                        .append(URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateHeader(authenticatedUser.getEmail()));
            String body = response.getResponseBody();
            log.info("Cross border summary response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve cross border summary. Status: {}", response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error retrieving cross border summary");
                }
            }

            ApiResponseJSON<CrossBorderSummaryResponse> apiResponse = objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<CrossBorderSummaryResponse>>() {
                    }
            );

            return apiResponse.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving order summary", e);
            throw new BadRequestException("Error retrieving order summary: " + e.getMessage());
        }
    }

    @Override
    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }
}
