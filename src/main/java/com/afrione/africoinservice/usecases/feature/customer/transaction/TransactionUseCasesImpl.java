package com.afrione.africoinservice.usecases.feature.customer.transaction;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.otc.*;
import com.afrione.africoinservice.usecases.exceptions.APIRequestException;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionUseCasesImpl implements TransactionUseCases{

    private final ApplicationProperty applicationProperty;

    private final RestClientService restClientService;

    private final ObjectMapper objectMapper;

    private final AppUserEntityDao appUserEntityDao;




    private String getAdminUsername(Long accountId) {
        AppUserEntity user = appUserEntityDao.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Admin user not found for account: " + accountId));
        //user.getFirstName() + " " + user.getLastName();
        return  user.getEmail();
    }
    @Override
    public ApplicationProperty getApplicationProperty() {
        return applicationProperty;
    }

    @Override
    public AmountSummaryResponse getAfriAndPaymentSummaryForFunding(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/funding-summary", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(1)
                    .append("&pageSize=").append(1);

            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

               url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    AmountSummaryResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for funding transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for funding transactions " + e.getMessage());
        }
    }

    @Override
    public AmountSummaryResponse getAfriAndPaymentSummaryForTransfer(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser) {

        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/transfer-summary", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(1)
                    .append("&pageSize=").append(1);

            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for transfer transactions. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for transfer transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    AmountSummaryResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to retrieve amount summary for transfer transactions", e);
            throw new BadRequestException("failed to retrieve amount summary for transfer transactions " + e.getMessage());
        }
    }

    @Override
    public AmountSummaryResponse getAfriAndPaymentSummaryForPayout(LocalDate startDate, LocalDate endDate, String countryCode, AuthenticatedUser authenticatedUser) {
        try{
            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/payout-summary", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(1)
                    .append("&pageSize=").append(1);

            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to retrieve amount summary for payout transactions. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to retrieve amount summary for payout transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    AmountSummaryResponse.class
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving customer transactions", e);
            throw new BadRequestException("Error retrieving customer transactions: " + e.getMessage());
        }
    }

    @Override
    public PagedResponse<CoinFundingTransactionResponse> fetchCoinFundingTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize, AuthenticatedUser authenticatedUser) {

        try{

            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/funding", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);


            if(startDate != null) {
                url.append("&startDate=").append(URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8));
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to fetch funding transactions: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to fetch funding transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<PagedResponse<CoinFundingTransactionResponse>>() {
                    }
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("failed to fetch funding transactions", e);
            throw new BadRequestException("failed to fetch funding transactions: " + e.getMessage());
        }

    }

    @Override
    public PagedResponse<CoinTransferTransactionResponse> fetchTransferTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize, AuthenticatedUser authenticatedUser) {
        try{

            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/transfer", applicationProperty.customerServiceUrl()));
            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);


            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("failed to fetch transfer transactions. Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("failed to fetch transfer transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<PagedResponse<CoinTransferTransactionResponse>>() {
                    }
            );



        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving customer transactions", e);
            throw new BadRequestException("Error retrieving customer transactions: " + e.getMessage());
        }

    }

    @Override
    public PagedResponse<CoinPayoutTransactionResponse> fetchPayoutTransactions(LocalDate startDate, LocalDate endDate, String countryCode, int pageNo, int pageSize, AuthenticatedUser authenticatedUser) {
        try{

            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/payout", applicationProperty.customerServiceUrl()));

            url.append("?pageNo=").append(pageNo)
                    .append("&pageSize=").append(pageSize);


            if(startDate != null) {
                url.append("&startDate=").append(startDate.toString());
            }

            if(endDate != null) {
                url.append("&endDate=").append(endDate.toString());
            }

            if(countryCode != null && !countryCode.isBlank()  ){

                url.append("&countryCode=").append(countryCode);
            }

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve payout transactions Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve payout transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    new TypeReference<PagedResponse<CoinPayoutTransactionResponse>>() {
                    }
            );

        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to retrieve payout transactions", e);
            throw new BadRequestException("Failed to retrieve payout transactions: " + e.getMessage());
        }
    }


    @Override
    public CoinPayoutTransactionResponse fetchPayoutTransactionDetails(Long id, AuthenticatedUser authenticatedUser) {
        try{

            StringBuilder url = new StringBuilder(String.format("%s/api/v1/admin/transaction/payout/%s", applicationProperty.customerServiceUrl(), id));

            RestClientResponse response = restClientService.getRequest(url.toString(), generateClientHeader(getAdminUsername(authenticatedUser.getUserId())));

            if (!isSuccessful(response.getStatusCode())) {
                log.warn("Failed to retrieve payout transactions Status: {}", response.getStatusCode());
                String body = response.getResponseBody();
                if (body != null && !body.isBlank()) {
                    APIRequestHandler.handleErrorResponse(response);
                } else {
                    throw new BadRequestException("Failed to retrieve payout transactions");
                }
            }

            String responseBody = response.getResponseBody();
            log.info("Response Body: {}", responseBody);

            return objectMapper.readValue(
                    responseBody,
                    CoinPayoutTransactionResponse.class
            );

        }catch (BadRequestException | APIRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to retrieve payout transactions", e);
            throw new BadRequestException("Failed to retrieve payout transactions: " + e.getMessage());
        }
    }



}
