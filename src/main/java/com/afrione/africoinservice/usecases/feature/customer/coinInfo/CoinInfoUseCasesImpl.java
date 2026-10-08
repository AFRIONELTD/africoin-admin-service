package com.afrione.africoinservice.usecases.feature.customer.coinInfo;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.data.response.otc.CoinInfoResponse;
import com.afrione.africoinservice.usecases.exceptions.APIRequestException;
import com.afrione.africoinservice.usecases.exceptions.BadRequestException;
import com.afrione.africoinservice.utils.APIRequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinInfoUseCasesImpl implements CoinInfoUseCases{


    private final ApplicationProperty applicationProperty;

    private final RestClientService restClientService;

    private final ObjectMapper objectMapper;

    private final AppUserEntityDao appUserEntityDao;


    public ApiResponseJSON<CoinInfoResponse> getAfriCoinInCirculation(AuthenticatedUser authenticatedUser) {
         try{

             String url = String.format("%s/api/v1/admin/coin/info", applicationProperty.customerServiceUrl());

             Map<String, String> headers = generateClientHeader(getAdminUsername(authenticatedUser.getAccountId()));

             RestClientResponse response = restClientService.getRequest(url, headers);

             if(!isSuccessful(response.getStatusCode())){
                 log.error("Error fetching AfriCoin in circulation: {}", response.getResponseBody());
                 String body = response.getResponseBody();

                 if(body != null && !body.isBlank()){

                     APIRequestHandler.handleErrorResponse(response);
                 }else{

                     throw new APIRequestException(HttpStatus.BAD_REQUEST, "Error fetching AfriCoin in circulation: Empty response body");
                 }

             }
             String responseBody = response.getResponseBody();
             log.info("Successfully fetched AfriCoin in circulation: {}", responseBody);

             return objectMapper.readValue(responseBody,
                     new TypeReference<ApiResponseJSON<CoinInfoResponse>>() {
                     });

         }catch (Exception e){
             throw new RuntimeException("Error fetching AfriCoin in circulation: " + e.getMessage(), e);
         }

    }


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
}
