package com.afrione.africoinservice.usecases.data.request;

import com.afrione.africoinservice.domain.entities.enums.CurrencyTypeConstant;

import java.math.BigDecimal;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
public record B2cExchangeRateUpdateRequest(
        String fiatCurrency,
        BigDecimal onRampRate,
        BigDecimal offRampRate,Boolean sendNotification,  String reason){
}
