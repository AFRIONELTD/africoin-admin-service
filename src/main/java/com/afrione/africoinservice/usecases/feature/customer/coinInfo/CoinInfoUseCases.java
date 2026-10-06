package com.afrione.africoinservice.usecases.feature.customer.coinInfo;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.ExternalRequestUseCases;
import com.afrione.africoinservice.usecases.data.response.otc.CoinBalanceResponse;
import com.afrione.africoinservice.usecases.data.response.otc.CoinCirculationResponse;

public interface CoinInfoUseCases extends ExternalRequestUseCases {

    ApiResponseJSON<CoinCirculationResponse> getAfriCoinInCirculation(AuthenticatedUser authenticatedUser);
    ApiResponseJSON<CoinBalanceResponse> getBridgeCoinBalance(AuthenticatedUser authenticatedUser);

}


