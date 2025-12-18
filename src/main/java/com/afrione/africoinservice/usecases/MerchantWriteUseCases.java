package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.domain.entities.enums.CryptoCurrencyTypeConstant;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.AdminMerchantResponse;
import com.afrione.africoinservice.usecases.data.response.merchant.CryptoFiatRateResponse;

import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
public interface MerchantWriteUseCases {

    void updateRates(List<ExchangeRateUpdateRequest> list, String cryptoCurrency, Long accountId);

    void approveOrDeclineMerchantDocument(KycApprovalRequest request, Long accountId);
}

