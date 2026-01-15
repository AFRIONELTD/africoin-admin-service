package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Created by felixadewale on
 * 13/01/2026
 */

@Getter
@Builder
public class OrderSummaryResponse {

    private String coinType;
    private BigDecimal coinValue;
    private String corridor;
    private BigDecimal corridorValue;
}
