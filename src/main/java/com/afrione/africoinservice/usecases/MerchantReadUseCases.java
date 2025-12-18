package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoFiatRateResponse;

import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
public interface MerchantReadUseCases extends ExternalRequestUseCases {
    PagedResponse<AdminMerchantResponse> retrieveMerchants(int pageNo, int pageSize, Long accountId);
    AdminMerchantResponse retrieveMerchant(String merchantId, AdminMerchantResponse.DetailLevel detailLevel, Long accountId);
    List<CryptoFiatRateResponse> retrieveRate(String cryptoCurrency, Long accountId);
}
