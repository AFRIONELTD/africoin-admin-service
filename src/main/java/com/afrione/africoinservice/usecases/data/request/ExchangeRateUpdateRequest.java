package com.afrione.africoinservice.usecases.data.request;

import com.afrione.africoinservice.domain.entities.enums.CryptoCurrencyTypeConstant;
import com.afrione.africoinservice.domain.entities.enums.CurrencyTypeConstant;

import java.math.BigDecimal;

/**
 * Created by felixadewale on
 * 10/12/2025
 */
public record ExchangeRateUpdateRequest(
        CryptoCurrencyTypeConstant cryptoCurrency,
        CurrencyTypeConstant fiatCurrency,
        BigDecimal newRate,
        BigDecimal markup){
}
