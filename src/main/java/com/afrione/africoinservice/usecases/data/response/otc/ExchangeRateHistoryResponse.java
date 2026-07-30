package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRateHistoryResponse {

    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal previousOnRampRate;
    private BigDecimal newOnRampRate;
    private BigDecimal previousOffRampRate;
    private BigDecimal newOffRampRate;
    private String reason;
    private String updatedBy;
    private String updatedAt;
}
