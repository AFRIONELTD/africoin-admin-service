package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoRateResponse;

import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
public interface MerchantReadUseCases extends ExternalRequestUseCases {
    PagedResponse<AdminMerchantResponse> retrieveMerchants(String searchTerm, String searchStatus, String countryCode, int pageNo, int pageSize, Long accountId);
    AdminMerchantResponse retrieveMerchant(String merchantId, AdminMerchantResponse.DetailLevel detailLevel, Long accountId);
    List<CryptoRateResponse> retrieveRate(Long accountId);
}
