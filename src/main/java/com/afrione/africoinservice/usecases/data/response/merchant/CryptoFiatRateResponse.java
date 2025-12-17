package com.afrione.africoinservice.usecases.data.response.merchant;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("cryptoCurrencyCode")
    private String cryptoCurrency;

    @JsonProperty("fiatCurrencyCode")
    private String fiatCurrency;

    @JsonProperty("fiatRate")
    private BigDecimal rate;
    private long lastUpdated;
}

