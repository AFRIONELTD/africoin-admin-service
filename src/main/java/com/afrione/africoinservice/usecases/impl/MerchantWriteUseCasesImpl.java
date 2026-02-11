package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.MerchantWriteUseCases;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    private final ApplicationProperty applicationProperty;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public void updateRates(List<ExchangeRateUpdateRequest> requests, String cryptoCurrency, Long accountId) {
        try {
            String url = String.format("%s/api/admin/v1/update-exchange-rate/" + cryptoCurrency, applicationProperty.merchantServiceUrl());
            ExchangeRateHelper.updateRate(restClientService, objectMapper, url, requests, generateMerchantHeader(getAdminUsername(accountId)));
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
            String url = String.format("%s/api/admin/v1/approve-or-decline-document", applicationProperty.merchantServiceUrl());

            String requestPayload = objectMapper.writeValueAsString(request);

            Map<String, String> headers = generateMerchantHeader(getAdminUsername(accountId));

            RestClientResponse response = restClientService.postRequest(url, requestPayload, headers);

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to process KYC approval for merchant {} and fileId {}. Status: {}",
                        request.getMerchantId(), request.getFileId(), response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestErrorHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to process KYC approval");
                }
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
