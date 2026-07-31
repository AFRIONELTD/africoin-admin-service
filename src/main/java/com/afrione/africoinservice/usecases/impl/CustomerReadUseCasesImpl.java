package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.JWTService;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.RateStatsModel;
import com.afrione.africoinservice.usecases.data.response.otc.*;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

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
                    APIRequestHandler.handleErrorResponse(response);
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
    public UserKycDetailModel getAppUser(String userId, Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/verification/user/%s", applicationProperty.customerServiceUrl(), userId);

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer {} from customer service. Status: {}", userId, response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("user not found");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            ApiResponseJSON<UserKycDetailModel> apiResponseJSON = objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<UserKycDetailModel>>() {
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

    @Override
    public RateStatsModel retrieveRateStats(String fiat, Long accountId) {
        try {
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/exchange-rate/stats", applicationProperty.customerServiceUrl()));

            if (fiat != null && !fiat.isBlank()) {
                url.append("?fiat=").append(URLEncoder.encode(fiat, StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve rate stats from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve rate stats");
                }
            }

            String responseBody = response.getResponseBody();
            ApiResponseJSON<RateStatsModel> apiResponseJSON = objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<RateStatsModel>>() {
                    }
            );

            RateStatsModel stats = apiResponseJSON.getData();
            log.info("Successfully retrieved rate stats");
            return stats;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving rate stats", e);
            throw new BadRequestException("Error retrieving rate stats: " + e.getMessage());
        }
    }


    @Override
    public List<B2cCryptoRateResponse> retrieveRate(Long accountId) {
        try {
            String url = String.format("%s/api/v1/admin/exchange-rate/retrieve", applicationProperty.customerServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(accountId)));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve rates from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve rates");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            ApiResponseJSON<List<B2cCryptoRateResponse>> apiResponseJSON =
                    objectMapper.readValue(
                            responseBody,
                            new TypeReference<ApiResponseJSON<List<B2cCryptoRateResponse>>>() {
                            }
                    );

            List<B2cCryptoRateResponse> rates = apiResponseJSON.getData();

            log.info("Successfully retrieved rates for {}", rates.size());
            return rates;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving rates for", e);
            throw new BadRequestException("Error retrieving rates: " + e.getMessage());
        }
    }


    public PagedResponse<AppUserModel> getActiveUsers(String searchKey, String countryCode, int pageNo, int pageSize, AuthenticatedUser authenticatedUser) {
        try {
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/user", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);


            if(searchKey != null && !searchKey.isBlank()) {
                url.append("&searchKeyword=").append(URLEncoder.encode(searchKey, StandardCharsets.UTF_8));
            }

            if(countryCode != null && !countryCode.isBlank()) {
                url.append("&countryCode=").append(URLEncoder.encode(countryCode, StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve active users from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve active users");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            PagedResponse<AppUserModel> apiResponseJSON =
                    objectMapper.readValue(
                            responseBody,
                            new TypeReference<PagedResponse<AppUserModel>>() {
                            }
                    );

           return apiResponseJSON;


        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving active users", e);
            throw new BadRequestException("Error retrieving active users: " + e.getMessage());
        }
    }


    public PagedResponse<ExchangeRateHistoryResponse> fetchExchangeRateHistory(String fiatCurrency, String cryptoCurrency, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize,
                                                                               AuthenticatedUser authenticatedUser) {
        try {
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/exchange-rate/history", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);

            if (fiatCurrency != null && !fiatCurrency.isBlank()) {
                url.append("&fiatCurrency=").append(URLEncoder.encode(fiatCurrency, StandardCharsets.UTF_8));
            }

            if (cryptoCurrency != null && !cryptoCurrency.isBlank()) {
                url.append("&cryptoCurrency=").append(URLEncoder.encode(cryptoCurrency, StandardCharsets.UTF_8));
            }

            if (startDate != null) {
                url.append("&startDate=").append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
            }

            if (endDate != null) {
                url.append("&endDate=").append(URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getAccountId())));

            if (!response.getStatusCode().is2xxSuccessful() ) {
                log.warn("Failed to retrieve exchange rate history from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve exchange rate history");
                }
            }

            String responseBody = response.getResponseBody();

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<PagedResponse<ExchangeRateHistoryResponse>>() {
                    }
            );
        }catch (Exception e){

            log.error("Error retrieving exchange rate history", e);
            throw new BadRequestException("Error retrieving exchange rate history: " + e.getMessage());
        }


    }


    public PagedResponse<CoinTransactionResponse> getCustomerTransactions(String userId, String transactionType,
                                                                          LocalDate startDate,LocalDate endDate,  int pageNo , int pageSize, AuthenticatedUser authenticatedUser) {

        try{

            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/%s", applicationProperty.customerServiceUrl(), userId));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);

            if(transactionType != null && !transactionType.isBlank()) {
                url.append("&transactionType=").append(URLEncoder.encode(transactionType, StandardCharsets.UTF_8));
            }

            if(startDate != null) {
                url.append("&startDate=").append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
            }

            if(endDate != null) {
                url.append("&endDate=").append(URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8));
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve customer transactions from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve customer transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);
            PagedResponse<CoinTransactionResponse> apiResponseJSON =
                    objectMapper.readValue(
                            responseBody,
                            new TypeReference<PagedResponse<CoinTransactionResponse>>() {
                            }
                    );

           return apiResponseJSON;



        }catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving customer transactions", e);
            throw new BadRequestException("Error retrieving customer transactions: " + e.getMessage());
        }


    }



    public List<WalletModel> getUserWallets(String userId, AuthenticatedUser authenticatedUser) {
        try {
            String url = String.format("%s/api/v1/admin/wallet/%s", applicationProperty.customerServiceUrl(), userId);

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve user wallets from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve user wallets");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<List<WalletModel>>() {
                    }
            );

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving user wallets", e);
            throw new BadRequestException("Error retrieving user wallets: " + e.getMessage());
        }
    }



    public List<PayoutProcessorInfoModel> getSupportedCorridors(){

        try{
            String url = String.format("%s/api/v1/common/supported-payout-corridors", applicationProperty.customerServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader("system"));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve user wallets from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve user wallets");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            ApiResponseJSON<List<PayoutProcessorInfoModel>> responseObject =  objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<List<PayoutProcessorInfoModel>>>() {
                    }
            );

            return responseObject.getData();
        }catch (BadRequestException e){
          throw e;
        }catch (Exception e){
            throw new BadRequestException("Error retrieving supported corridors: " + e.getMessage());
        }

    }

    public List<WalletCurrencyModel> getSupportedWalletCurrencies(){

        try{
            String url = String.format("%s/api/v1/common/crypto-type", applicationProperty.customerServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader("system"));

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Failed to retrieve supported wallet currencies from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve supported wallet currencies");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            ApiResponseJSON<List<WalletCurrencyModel>> responseObject =  objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<List<WalletCurrencyModel>>>() {
                    }
            );

            return responseObject.getData();
        }catch (BadRequestException e){
          throw e;
        }catch (Exception e){
            throw new BadRequestException("Error retrieving supported wallet currencies: " + e.getMessage());
        }

    }


    public List<CountryModel> getActiveCountries(){

        try{

            String url = String.format("%s/api/v1/common/selected-countries", applicationProperty.customerServiceUrl());

            RestClientResponse response = restClientService.getRequest(url, generateClientHeader("system"));

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Failed to retrieve active countries from customer service. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve active countries");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            ApiResponseJSON<List<CountryModel>> responseObject =  objectMapper.readValue(
                    responseBody,
                    new TypeReference<ApiResponseJSON<List<CountryModel>>>() {
                    }
            );

            return responseObject.getData();


        }catch (BadRequestException e){
          throw e;
        }catch (Exception e){

            throw new BadRequestException("Error retrieving active countries: " + e.getMessage());
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
