package com.afrione.africoinservice.usecases.feature.customer.coinInfo;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.ExternalRequestUseCases;
import com.afrione.africoinservice.usecases.data.response.otc.CoinInfoResponse;

public interface CoinInfoUseCases extends ExternalRequestUseCases {

    ApiResponseJSON<CoinInfoResponse> getAfriCoinInCirculation(AuthenticatedUser authenticatedUser);


}


