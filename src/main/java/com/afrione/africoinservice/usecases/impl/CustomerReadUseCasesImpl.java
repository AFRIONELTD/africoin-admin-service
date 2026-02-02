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
import com.afrione.africoinservice.usecases.data.response.otc.AppUserModel;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    public PagedResponse<AppUserModel> listUsers(String searchTerm, String searchStatus, int pageNo, int pageSize, Long accountId) {
        try {
            StringBuilder urlBuilder = new StringBuilder(applicationProperty.customerServiceUrl() + "/api/v1/admin/verification/users");
            urlBuilder.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);

            if (StringUtils.isNotBlank(searchTerm)) {
                urlBuilder.append("&searchTerm=").append(URLEncoder.encode(searchTerm, StandardCharsets.UTF_8));
            }

            if(StringUtils.isNotBlank(searchStatus)){
                urlBuilder.append("&searchStatus=").append(URLEncoder.encode(searchStatus, StandardCharsets.UTF_8));
            }

            String url = urlBuilder.toString();

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(accountId)));
            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer {} from customers service. Status:", response.getStatusCode());
                if (responseBody != null && !responseBody.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error retrieving users");
                }
            }

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

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer {} from customer service. Status: {}", userId, response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("user not found");
                }
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


    @Override
    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }
}
