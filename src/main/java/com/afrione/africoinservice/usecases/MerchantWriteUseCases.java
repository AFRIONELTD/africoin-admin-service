package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.afrione.africoinservice.usecases.data.request.KycApprovalRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by felixadewale on
 * 10/12/2025
 */

public interface MerchantWriteUseCases extends ExternalRequestUseCases {

    void updateRates(List<ExchangeRateUpdateRequest> list, String cryptoCurrency, Long accountId);

    void approveOrDeclineMerchantDocument(KycApprovalRequest request, Long accountId);

    default Map<String, String> generateHeader(String adminUser) {
        System.out.println("Generating headers for admin user: " + adminUser);
        Map<String, String> headers = new HashMap<>();
        headers.put("x-request-client-key", getApplicationProperty().getB2BRequestClientKey());
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
    ApplicationProperty getApplicationProperty();

}

