package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by jnwanya on
 * Sun, 05 Oct, 2025
 */
@Builder
@Data
public class B2cCryptoRateResponse {
    private String cryptoCurrencyCode;
    private FiatCurrencyModel fiatCurrencyCode;
    private BigDecimal onRampRate;
    private BigDecimal offRampRate;
    private String created;
    private String createdBy;
}
