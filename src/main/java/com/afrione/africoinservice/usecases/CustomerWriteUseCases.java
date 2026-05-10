package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.response.otc.UserDocReviewRequest;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * Created by felixadewale on
 * 15/12/2025
 */
public interface CustomerWriteUseCases extends ExternalRequestUseCases {
    String reviewUserDocument(UserDocReviewRequest request, Long accountId);

    void updateRates(List<ExchangeRateUpdateRequest> exchangeRateUpdateRequests, String cryptoCurrency, Long accountId);
}
