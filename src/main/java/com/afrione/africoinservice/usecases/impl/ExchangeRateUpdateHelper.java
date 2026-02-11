package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.services.RestClientService;
import com.afrione.africoinservice.usecases.data.request.ExchangeRateUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Backwards-compatible delegator to the centralized ExchangeRateHelper.
 * Kept to avoid needing to update all historical callsites; forwards to ExchangeRateHelper.updaterate.
 */
public final class ExchangeRateUpdateHelper {

    private ExchangeRateUpdateHelper() {}

    public static void sendUpdate(RestClientService restClientService,
                                  ObjectMapper objectMapper,
                                  String url,
                                  List<ExchangeRateUpdateRequest> requests,
                                  Map<String, String> headers) {
        ExchangeRateHelper.updateRate(restClientService, objectMapper, url, requests, headers);
    }
}
