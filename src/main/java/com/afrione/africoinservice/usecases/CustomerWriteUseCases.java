package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.infrastructure.web.controllers.RateController;
import com.afrione.africoinservice.usecases.data.request.B2cExchangeRateUpdateRequest;
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

    void updateRates(B2cExchangeRateUpdateRequest req, String cryptoCurrency, Long accountId);
}
