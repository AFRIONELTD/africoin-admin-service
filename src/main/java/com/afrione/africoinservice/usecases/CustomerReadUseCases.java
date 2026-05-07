package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.RateStatsModel;
import com.afrione.africoinservice.usecases.data.response.otc.AppUserModel;
import com.afrione.africoinservice.usecases.data.response.otc.UserKycDetailModel;

import java.util.List;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerReadUseCases extends ExternalRequestUseCases {
    PagedResponse<AppUserModel> listUsers(String searchTerm, String searchStatus, int pageNo, int pageSize, Long accountId);
    UserKycDetailModel getAppUser(String userId, Long accountId);

    RateStatsModel retrieveRateStats(String fiat, Long accountId);

    List<CryptoRateResponse> retrieveRate(Long accountId);
}
