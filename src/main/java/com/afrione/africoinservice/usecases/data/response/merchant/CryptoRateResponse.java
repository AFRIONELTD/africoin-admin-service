package com.afrione.africoinservice.usecases.data.response.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jnwanya on
 * Sun, 05 Oct, 2025
 */
@Builder
@Data
public class CryptoRateResponse {
    private CountryModel country;
    private List<CryptoRateByType> cryptoRates;

    @Builder
    @Data
    public static class CryptoRateByType {
        private String cryptoCurrencyCode;
        private BigDecimal fiatRate;
        private BigDecimal rateMarkup;
        private BigDecimal previousFiatRate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastUpdate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime created;
    }
}
