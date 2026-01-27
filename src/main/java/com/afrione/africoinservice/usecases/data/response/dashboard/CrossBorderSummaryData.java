package com.afrione.africoinservice.usecases.data.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by felixadewale on
 * 26/01/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrossBorderSummaryData {
    private String currency;
    private String country;
    private BigDecimal recipientAmount;


}
