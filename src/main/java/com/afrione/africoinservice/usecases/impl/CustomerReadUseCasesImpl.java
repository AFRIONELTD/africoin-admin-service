package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.customer.AppUserModel;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by felixadewale on
 * 15/12/2025
 */

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomerReadUseCasesImpl implements CustomerReadUseCases {

    private final JWTService jwtService;
    private final ApplicationProperty applicationProperty;
    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final AppUserEntityDao appUserEntityDao;

    @Override
    public PagedResponse<AppUserModel> listUsers(int pageNo, int pageSize, Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/verification/users", applicationProperty.customerServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer {} from customers service. Status:", response.getStatusCode());
                throw new BadRequestException("Merchant not found");
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            ApiResponseJSON<PagedResponse<AppUserModel>> apiResponseJSON = objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<PagedResponse<AppUserModel>>>() {
                    }
            );
            log.info("Successfully retrieved customers");
            return apiResponseJSON.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving customers", e);
            throw new BadRequestException("Error retrieving customers: " + e.getMessage());
        }
    }

    @Override
    public AppUserModel getAppUser(String userId, Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/verification/user/%s", applicationProperty.customerServiceUrl(), userId);

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer {} from customer service. Status: {}", userId, response.getStatusCode());
                throw new BadRequestException("Merchant not found");
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            ApiResponseJSON<AppUserModel> apiResponseJSON = objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<AppUserModel>>() {
                    }
            );

            log.info("Successfully retrieved customer: {}", userId);
            return apiResponseJSON.getData();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving customer: {}", userId, e);
            throw new BadRequestException("Error retrieving customer: " + e.getMessage());
        }
    }


    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        return user.getFirstName() + " " + user.getLastName();
    }


    private Map<String, String> generateHeader(String adminUser) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(AppConstant.TOKEN_TYPE, AppConstant.TOKEN_TYPE_ACCESS);
        attributes.put("accountType", "merchant_admin_app");
        attributes.put("userId", adminUser);
        attributes.put("accountId", adminUser);

        String token = adminUser+applicationProperty.getClientTokenSecretKey();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("x-request-client-key", applicationProperty.getB2CRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private boolean isSuccessful(HttpStatusCode statusCode) {
        return statusCode != null && statusCode.is2xxSuccessful();
    }
}
