package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;

import java.util.List;

/**
 * Created by felixadewale on
 * 10/12/2025
 */

public interface MerchantWriteUseCases extends ExternalRequestUseCases {

    void updateRates(List<ExchangeRateUpdateRequest> list, String cryptoCurrency, Long accountId);

    void approveOrDeclineMerchantDocument(KycApprovalRequest request, Long accountId);

}

