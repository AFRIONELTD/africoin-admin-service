package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.MerchantWriteUseCases;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

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
public class MerchantWriteUseCasesImpl implements MerchantWriteUseCases {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;
    private final JWTService jwtService;
    private final ApplicationProperty applicationProperty;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public void updateRates(List<ExchangeRateUpdateRequest> requests, Long accountId) {
        try {
            if (requests == null || requests.isEmpty()) {
                throw new BadRequestException("At least one exchange rate update is required");
            }

            String url = String.format("%s/api/admin/v1/update-exchange-rate/update", applicationProperty.merchangetServiceUrl());

            String requestPayload = objectMapper.writeValueAsString(requests);

            Map<String, String> headers = new HashMap<>();


            log.info("Sending exchange rate update request for {} currency pairs", requests.size());

            RestClientResponse response = restClientService.postRequest(url, requestPayload, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to update exchange rates. Status: {}", response.getStatusCode());
                throw new BadRequestException("Failed to update exchange rates");
            }

            log.info("Successfully updated {} exchange rates", requests.size());
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating exchange rates", e);
            throw new BadRequestException("Error updating exchange rates: " + e.getMessage());
        }
    }

    @Override
    public void approveOrDeclineMerchantDocument(KycApprovalRequest request, Long accountId) {
        try {
            String url = String.format("%s/api/admin/v1/approve-or-decline-document/kyc/approve-or-decline", applicationProperty.merchangetServiceUrl());

            String requestPayload = objectMapper.writeValueAsString(request);

            Map<String, String> headers = generateHeader(getAdminUsername(accountId));

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to process KYC approval for merchant {} and fileId {}. Status: {}",
                        request.getMerchantId(), request.getFileId(), response.getStatusCode());
                throw new BadRequestException("Failed to process KYC approval");
            }

            log.info("KYC document {} for merchant {} has been successfully {}",
                    request.getFileId(),
                    request.getMerchantId(),
                    request.isApproved() ? "APPROVED" : "DECLINED");

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing KYC approval for merchant: {}", request.getMerchantId(), e);
            throw new BadRequestException("Error processing KYC approval: " + e.getMessage());
        }
    }

    private boolean isSuccessful(HttpStatusCode statusCode) {
        return statusCode != null && statusCode.is2xxSuccessful();
    }

    private Map<String, String> generateHeader(String adminUser) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(AppConstant.TOKEN_TYPE, AppConstant.TOKEN_TYPE_ACCESS);
        attributes.put("accountType", "merchant_user");
        attributes.put("merchantUserId", adminUser);
        attributes.put("merchantId", adminUser);

        String token = jwtService.expiringToken(applicationProperty.getMerchantTokenSecretKey(), attributes, 3);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
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
