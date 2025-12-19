package com.afrione.africoinservice.usecases.data.response.merchant;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jnwanya on
 * Sun, 05 Oct, 2025
 */
@Builder
@Data
public class CryptoRateResponse {
    private String cryptoCurrencyCode;
    private List<CryptoRate> rates;

    @Builder
    @Data
    public static class CryptoRate {
        private String fiatCurrencyCode;
        private BigDecimal fiatRate;
        private BigDecimal rateMarkup;
    }
}
