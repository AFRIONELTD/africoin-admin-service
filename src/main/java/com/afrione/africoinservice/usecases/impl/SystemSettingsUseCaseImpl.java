package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.SystemSettingsUseCase;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.system_settings.SystemSettingsResponse;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by felixadewale on
 * 12/01/2026
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemSettingsUseCaseImpl implements SystemSettingsUseCase {

    private final RestClientService restClientService;
    private final AppUserEntityDao appUserEntityDao;
    private final ApplicationProperty applicationProperty;
    private final ObjectMapper objectMapper;


    @Override
    public void updateSettings(Long accountId, String system, Long settingId, String value) {
        String url;
        boolean isOTC = "OTC".equalsIgnoreCase(system);
        if ("OTC".equalsIgnoreCase(system)) {
            url = applicationProperty.customerServiceUrl() + "/api/v1/admin/system-settings/system-setting/" + settingId;
        } else if ("MERCHANT".equalsIgnoreCase(system)) {
            url = applicationProperty.merchantServiceUrl() + "/api/admin/v1/system-setting/" + settingId;
        } else {
            throw new BadRequestException("Unsupported system: " + system);
        }
        url = url + "?value=" + value;
        try {
            RestClientResponse response = restClientService.putRequest(url, null, generateHeader(getAdminUsername(accountId), isOTC));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to update {} settings {} from customers service. Status:", system, response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error update system settings");
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating system settings: ", e);
            throw new BadRequestException("Error updating system settings: " + e.getMessage());
        }


    }

    @Override
    public PagedResponse<SystemSettingsResponse> getSystemSettings(Long accountId, String system, int page, int size) {
        boolean isOTC = "OTC".equalsIgnoreCase(system);
        String url;
        if ("OTC".equalsIgnoreCase(system)) {
            url = applicationProperty.customerServiceUrl() + "/api/v1/admin/system-settings/system-settings" + "?page=" + page + "&size=" + size;
        } else if ("MERCHANT".equalsIgnoreCase(system)) {
            url = applicationProperty.merchantServiceUrl() + "/api/admin/v1/system-settings" + "?page=" + page + "&size=" + size;
        } else {
            throw new BadRequestException("Unsupported system: " + system);
        }
        try {
            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId), isOTC));
            String body = response.getResponseBody();
            log.info("Response Body: {}", body);
            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve {} settings {} from customers service. Status:", system, response.getStatusCode());
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Error update system settings");
                }
            }

            return objectMapper.readValue(
                    body,
                    new TypeReference<ApiResponseJSON<PagedResponse<SystemSettingsResponse>>>() {
                    }
            ).getData();
        } catch (Exception e) {
            log.error("Exception occurred while retrieving system settings: ", e);
            throw new BadRequestException("Error retrieving system settings: " + e.getMessage());
        }


    }

    private Map<String, String> generateHeader(String adminUsername, boolean isOTC) {
        return isOTC ? generateClientHeader(adminUsername) : generateMerchantHeader(adminUsername);
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

