package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.CustomerWriteUseCases;
import com.afrione.africoinservice.usecases.data.response.otc.UserDocReviewRequest;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CustomerWriteUseCasesImpl implements CustomerWriteUseCases {

    private final ApplicationProperty applicationProperty;
    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public void reviewUserDocument(UserDocReviewRequest request, Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/verification/review", applicationProperty.customerServiceUrl());

            String requestPayload = objectMapper.writeValueAsString(request);

            Map<String, String> headers = generateClientHeader(getAdminUsername(accountId));

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to process KYC approval for customer {} and verificationId {}. Status: {}",
                        request.getUserId(), request.getVerificationId(), response.getStatusCode());

                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to process Customer KYC approval");
                }

            }

            log.info("KYC document {} for customer {} has been successfully {}",
                    request.getVerificationId(),
                    request.getUserId(),
                    request.getIsApproved() == Boolean.TRUE ? "APPROVED" : "DECLINED");

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing KYC approval for customer: {}", request.getUserId(), e);
            throw new BadRequestException("Error processing KYC approval: " + e.getMessage());
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
