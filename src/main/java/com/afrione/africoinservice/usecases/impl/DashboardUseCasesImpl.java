package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.DashboardUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.dashboard.CrossBorderSummaryResponse;
import com.afrione.africoinservice.usecases.data.response.dashboard.OrderGraphResponse;
import com.afrione.africoinservice.usecases.data.response.otc.AppUserModel;
import com.afrione.africoinservice.usecases.data.response.otc.MonthStatsWrapper;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final AppUserEntityDao appUserEntityDao;



    public MonthStatsWrapper getMonthStats(Integer startMonth, Integer endMonth, Integer year, String countryCode, AuthenticatedUser authenticatedUser){

        try {
            StringBuilder urlBuilder = new StringBuilder(applicationProperty.customerServiceUrl() + "/api/v1/admin/dashboard/afri-month-graph");
            urlBuilder.append("?pageNo=").append(1)
                    .append("&pageSize=").append(1);

            if (startMonth != null) {

                urlBuilder.append("&startMonth=").append(startMonth);
            }

            if(endMonth != null){
                urlBuilder.append("&endMonth=").append(endMonth);
            }

            if(year != null){
                urlBuilder.append("&year=").append(year);
            }

            if(countryCode != null){
                urlBuilder.append("&countryCode=").append(countryCode);
            }

            String url = urlBuilder.toString();



            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(authenticatedUser.getAccountId())));
            String responseBody = response.getResponseBody();

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("error retrieving month stats {} . Status:", response.getStatusCode());
                if (responseBody != null && !responseBody.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("error retrieving month stats");
                }
            }

            return objectMapper.readValue(
                    responseBody,
                    MonthStatsWrapper.class
            );
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("error retrieving month stats ", e);
            throw new BadRequestException("error retrieving month stats " + e.getMessage());
        }
    }





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

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(authenticatedUser.getEmail()));
            String body = response.getResponseBody();
            log.info("Cross border summary response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve cross border summary. Status: {}", response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
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

    @Override
    public OrderGraphResponse getOrderGraphData(AuthenticatedUser authenticatedUser, LocalDate startDate, LocalDate endDate) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(getApplicationProperty().customerServiceUrl())
                    .append("/api/v2/admin/dashboard/order-graph");

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

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(authenticatedUser.getEmail()));
            String body = response.getResponseBody();
            log.info("graph data response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve order graph data. Status: {}", response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error retrieving order graph data ");
                }
            }

            ApiResponseJSON<OrderGraphResponse> apiResponse = objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<OrderGraphResponse>>() {
                    }
            );

            return apiResponse.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving order graph data", e);
            throw new BadRequestException("Error retrieving order graph data: " + e.getMessage());
        }
    }


    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        return user.getFirstName() + " " + user.getLastName();
    }
}
