package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.MerchantReadUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MerchantReadUseCasesImpl implements MerchantReadUseCases {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final ApplicationProperty applicationProperty;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public PagedResponse<AdminMerchantResponse> retrieveMerchants(String searchTerm, String searchStatus, String countryCode, int pageNo, int pageSize, Long accountId) {
        try {

            StringBuilder url = new StringBuilder(
                    String.format(
                            "%s/api/admin/v1/retrieve-merchants?pageNo=%d&pageSize=%d",
                            applicationProperty.merchantServiceUrl(),
                            pageNo,
                            pageSize
                    )
            );

            if (searchTerm != null && !searchTerm.isBlank()) {
                url.append("&searchTerm=").append(URLEncoder.encode(searchTerm, StandardCharsets.UTF_8));
            }

            if (searchStatus != null && !searchStatus.isBlank()) {
                url.append("&searchStatus=").append(searchStatus);
            }

            if (countryCode != null && !countryCode.isBlank()) {
                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve merchants from service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve merchants");
                }
            }

            String responseBody = response.getResponseBody();
            System.out.println(responseBody);
            @SuppressWarnings("unchecked")
            ApiResponseJSON<PagedResponse<AdminMerchantResponse>> apiResponseJSON = objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<PagedResponse<AdminMerchantResponse>>>() {
                    }
            );

            PagedResponse<AdminMerchantResponse> pagedResponse = apiResponseJSON.getData();
            log.info("Successfully retrieved {} merchants", pagedResponse.getRecords().size());
            return pagedResponse;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving merchants", e);
            throw new BadRequestException("Error retrieving merchants: " + e.getMessage());
        }
    }

    @Override
    public AdminMerchantResponse retrieveMerchant(String merchantId, AdminMerchantResponse.DetailLevel detailLevel, Long accountId) {
        try {
            String url = String.format("%s/api/admin/v1/retrieve-merchant/%s?detailLevel=%s", applicationProperty.merchantServiceUrl(), merchantId, detailLevel.name());

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve merchant {} from service. Status: {}", merchantId, response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Merchant not found");
                }
            }

            ApiResponseJSON<AdminMerchantResponse> merchantResponse = objectMapper.readValue(
                    response.getResponseBody(),
                    new TypeReference<ApiResponseJSON<AdminMerchantResponse>>() {
                    }
            );


            log.info("Successfully retrieved merchant: {}", merchantId);
            return merchantResponse.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving merchant: {}", merchantId, e);
            throw new BadRequestException("Error retrieving merchant: " + e.getMessage());
        }
    }

    @Override
    public List<CryptoRateResponse> retrieveRate(Long accountId) {
        try {
            String url = String.format("%s/api/admin/v1/retrieve-rates", applicationProperty.merchantServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve rates from service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve rates");
                }
            }

            String responseBody = response.getResponseBody();
            System.out.println(responseBody);
            ApiResponseJSON<List<CryptoRateResponse>> apiResponseJSON =
                    objectMapper.readValue(
                            responseBody,
                            new TypeReference<ApiResponseJSON<List<CryptoRateResponse>>>() {
                            }
                    );

            List<CryptoRateResponse> rates = apiResponseJSON.getData();

            log.info("Successfully retrieved rates for {}", rates.size());
            return rates;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving rates for", e);
            throw new BadRequestException("Error retrieving rates: " + e.getMessage());
        }
    }


    private Map<String, String> generateHeader(String adminUser) {
        log.info("Generating headers for admin user: {}", adminUser);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", applicationProperty.getB2BRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        return user.getFirstName() + " " + user.getLastName();
    }
}
