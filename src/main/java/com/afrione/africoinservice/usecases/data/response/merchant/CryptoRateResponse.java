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
    private String fiatCurrencyCode;
    private BigDecimal onRampRate;
    private BigDecimal offRampRate;
    private String created;
    private String createdBy;
}
