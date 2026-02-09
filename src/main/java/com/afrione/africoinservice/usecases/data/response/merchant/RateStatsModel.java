package com.afrione.africoinservice.usecases.data.response.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RateStatsModel {
    private long activeCurrencies;
    private BigDecimal avgMarkup;
    private String lastUpdated;

}

