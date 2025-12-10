package com.afrione.africoinservice.usecases.data.response.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoFiatRateResponse {
    private String cryptoCurrency;
    private String fiatCurrency;
    private BigDecimal rate;
    private long lastUpdated;
}

