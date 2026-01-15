package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.OrderHistoryUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderHistoryResponse;
import com.afrione.africoinservice.usecases.data.response.otc.OrderSummaryResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by felixadewale on
 * 15/01/2026
 */

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderHistoryUseCasesImpl implements OrderHistoryUseCases {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final ApplicationProperty applicationProperty;
    private final AppUserEntityDao appUserEntityDao;

    @Override
    public PagedResponse<OrderHistoryResponse> getOrderHistory(String orderType, LocalDate startDate, LocalDate endDate, String corridor, String status, int page, int size) {
        try {
            StringBuilder url = new StringBuilder();
            // Assuming OTC endpoints are hosted on customer service under /api/v1/admin/otc/orders
            url.append(applicationProperty.customerServiceUrl())
                    .append("/api/v1/admin/otc/orders/history")
                    .append("?orderType=").append(URLEncoder.encode(orderType, StandardCharsets.UTF_8))
                    .append("&page=").append(page)
                    .append("&size=").append(size);

            if (startDate != null) {
                url.append("&startDate=").append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
            }

            if (endDate != null) {
                url.append("&endDate=").append(URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8));
            }

            if (StringUtils.isNotBlank(corridor)) {
                url.append("&corridor=").append(URLEncoder.encode(corridor, StandardCharsets.UTF_8));
            }

            if (StringUtils.isNotBlank(status)) {
                url.append("&status=").append(URLEncoder.encode(status, StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateHeader());
            String body = response.getResponseBody();
            log.info("Order history response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve order history. Status: {}", response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error retrieving order history");
                }
            }

            ApiResponseJSON<PagedResponse<OrderHistoryResponse>> apiResponse = objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<PagedResponse<OrderHistoryResponse>>>() {
                    }
            );

            return apiResponse.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving order history", e);
            throw new BadRequestException("Error retrieving order history: " + e.getMessage());
        }
    }

    @Override
    public OrderHistoryResponse getOrderDetail(String orderId, String orderType) {
        try {
            // Assuming detail endpoint
            String url = String.format("%s/api/v1/admin/otc/orders/%s?orderType=%s", applicationProperty.customerServiceUrl(), URLEncoder.encode(orderId, StandardCharsets.UTF_8), URLEncoder.encode(orderType, StandardCharsets.UTF_8));

            RestClientResponse response = restClientService.getRequest(url, generateHeader());
            String body = response.getResponseBody();
            log.info("Order detail response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve order detail {}. Status: {}", orderId, response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Order not found");
                }
            }

            ApiResponseJSON<OrderHistoryResponse> apiResponse = objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<OrderHistoryResponse>>() {
                    }
            );

            return apiResponse.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving order detail: {}", orderId, e);
            throw new BadRequestException("Error retrieving order detail: " + e.getMessage());
        }
    }

    @Override
    public List<OrderSummaryResponse> getOrderSummary(String orderType, String corridor, LocalDate startDate, LocalDate endDate) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(applicationProperty.customerServiceUrl())
                    .append("/api/v1/admin/otc/orders/summary")
                    .append("?orderType=").append(URLEncoder.encode(orderType, StandardCharsets.UTF_8));

            if (StringUtils.isNotBlank(corridor)) {
                url.append("&corridor=").append(URLEncoder.encode(corridor, StandardCharsets.UTF_8));
            }

            if (startDate != null) {
                url.append("&startDate=").append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
            }

            if (endDate != null) {
                url.append("&endDate=").append(URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateHeader());
            String body = response.getResponseBody();
            log.info("Order summary response body: {}", body);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve order summary. Status: {}", response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error retrieving order summary");
                }
            }

            ApiResponseJSON<List<OrderSummaryResponse>> apiResponse = objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<List<OrderSummaryResponse>>>() {
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

    private Map<String, String> generateHeader() {
        log.info("Generating headers for OTC request");
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", applicationProperty.getB2CRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
