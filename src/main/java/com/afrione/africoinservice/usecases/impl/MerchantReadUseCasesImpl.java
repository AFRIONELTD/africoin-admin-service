package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.enums.CryptoCurrencyTypeConstant;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.MerchantReadUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoFiatRateResponse;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    private final JWTService jwtService;
    private final ApplicationProperty applicationProperty;
    private final AppUserEntityDao appUserEntityDao;


    @Override
    public PagedResponse<AdminMerchantResponse> retrieveMerchants(int pageNo, int pageSize, Long accountId) {
        try {
            String url = String.format("%s/api/admin/v1/retrieve-merchants?pageNo=%d&pageSize=%d", applicationProperty.merchangetServiceUrl(), pageNo, pageSize);

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve merchants from service. Status: {}", response.getStatusCode());
                throw new BadRequestException("Failed to retrieve merchants");
            }

            @SuppressWarnings("unchecked")
            PagedResponse<AdminMerchantResponse> pagedResponse = objectMapper.convertValue(
                    response.getResponseObject(),
                    objectMapper.getTypeFactory().constructParametricType(PagedResponse.class, AdminMerchantResponse.class)
            );

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
            String url = String.format("%s/api/admin/v1/retrieve-merchant/%s?detailLevel=%s", applicationProperty.merchangetServiceUrl(), merchantId, detailLevel.name());

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve merchant {} from service. Status: {}", merchantId, response.getStatusCode());
                throw new BadRequestException("Merchant not found");
            }

            AdminMerchantResponse merchantResponse = objectMapper.convertValue(
                    response.getResponseObject(),
                    AdminMerchantResponse.class
            );

            log.info("Successfully retrieved merchant: {}", merchantId);
            return merchantResponse;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving merchant: {}", merchantId, e);
            throw new BadRequestException("Error retrieving merchant: " + e.getMessage());
        }
    }

    @Override
    public List<CryptoFiatRateResponse> retrieveRate(String cryptoCurrency, Long accountId) {
        try {
            CryptoCurrencyTypeConstant cryptoCurrencyTypeConstant;
            try {
                cryptoCurrencyTypeConstant = CryptoCurrencyTypeConstant.valueOf(cryptoCurrency.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Unsupported crypto currency type: " + cryptoCurrency);
            }

            String url = String.format("%s/api/admin/v1/retrieve-rate/%s", applicationProperty.merchangetServiceUrl(), cryptoCurrencyTypeConstant.name());

            RestClientResponse response = restClientService.getRequest(url, generateHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve rates for {} from service. Status: {}", cryptoCurrencyTypeConstant, response.getStatusCode());
                throw new BadRequestException("Failed to retrieve rates");
            }

            CryptoFiatRateResponse[] rates = objectMapper.convertValue(
                    response.getResponseObject(),
                    CryptoFiatRateResponse[].class
            );

            log.info("Successfully retrieved {} rates for {}", rates.length, cryptoCurrencyTypeConstant);
            return Arrays.asList(rates);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving rates for: {}", cryptoCurrency, e);
            throw new BadRequestException("Error retrieving rates: " + e.getMessage());
        }
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

    private boolean isSuccessful(HttpStatusCode statusCode) {
        return statusCode != null && statusCode.is2xxSuccessful();
    }

    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        return user.getFirstName() + " " + user.getLastName();
    }
}
