package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.CustomerWriteUseCases;
import com.afrione.africoinservice.usecases.data.response.customer.UserDocReviewRequest;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
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
public class CustomerWriteUseCasesImpl implements CustomerWriteUseCases {

    private final JWTService jwtService;
    private final ApplicationProperty applicationProperty;
    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public void reviewUserDocument(UserDocReviewRequest request, Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/verification/review", applicationProperty.customerServiceUrl());

            String requestPayload = objectMapper.writeValueAsString(request);

            Map<String, String> headers = generateHeader(getAdminUsername(accountId));

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to process KYC approval for customer {} and verificationId {}. Status: {}",
                        request.getUserId(), request.getVerificationId(), response.getStatusCode());
                throw new BadRequestException("Failed to process Customer KYC approval");
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


    private Map<String, String> generateHeader(String adminUser) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(AppConstant.TOKEN_TYPE, AppConstant.TOKEN_TYPE_ACCESS);
        attributes.put("accountType", "merchant_admin_app");
        attributes.put("userId", adminUser);
        attributes.put("accountId", adminUser);

        String token = jwtService.expiringToken(applicationProperty.geCustomerTokenSecretKey(), attributes, 3);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("x-request-client-key", applicationProperty.getB2CRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}

